package com.example.gameguesser.ui.chatbot
import com.example.gameguesser.BuildConfig
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Response
import java.util.concurrent.TimeUnit


class ChatbotViewModel(private val appContext: Context? = null) : ViewModel() {
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val viewModelJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun displayName(sender: ChatMessage.Sender): String =
        when (sender) {
            ChatMessage.Sender.USER -> "You"
            ChatMessage.Sender.BOT -> "Tiny"
        }

    fun sendMessage(text: String) {
        val current = _messages.value ?: emptyList()
        _messages.value = current + ChatMessage(ChatMessage.Sender.USER, text)

        scope.launch {
            val apiKey = BuildConfig.OPENROUTER_API_KEY ?: ""
            val isOnline = isNetworkAvailable()
            val replyText = if (apiKey.isNotBlank() && isOnline) {
                callOpenRouterApi(apiKey, text)
            } else {
                null
            }

            val finalReply = replyText ?: mockReply(text)
            _messages.value = (_messages.value ?: emptyList()) + ChatMessage(ChatMessage.Sender.BOT, finalReply)
        }
    }

    private fun mockReply(input: String): String {
        return "I heard: \"$input\""
    }

    private suspend fun callOpenRouterApi(apiKey: String, userText: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BASIC

                val authInterceptor = Interceptor { chain ->
                    val req = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer $apiKey")
                        .addHeader("Content-Type", "application/json")
                        .build()
                    chain.proceed(req)
                }

                val client = OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .addInterceptor(logging)
                    .callTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("https://openrouter.ai/api/v1/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val api = retrofit.create(OpenRouterApi::class.java)

                val content = OpenRouterMessageContent(text = userText)
                val message = OpenRouterMessage(role = "user", content = listOf(content))
                val request = OpenRouterRequest(model = "openai/gpt-oss-20b:free", messages = listOf(message))

                val resp: Response<Map<String, Any>> = api.createChat(request)

                if (!resp.isSuccessful) {
                    // optional: you can log resp.errorBody()
                    return@withContext null
                }

                val body = resp.body() ?: return@withContext null

                // Defensive parsing: extract textual result from many possible structures
                val choices = body["choices"]
                if (choices is List<*>) {
                    val firstChoice = choices.firstOrNull()
                    if (firstChoice is Map<*, *>) {
                        val messageObj = firstChoice["message"]
                        if (messageObj is Map<*, *>) {
                            val contentObj = messageObj["content"]
                            if (contentObj is List<*>) {
                                val sb = StringBuilder()
                                contentObj.forEach { item ->
                                    if (item is Map<*, *>) {
                                        val t = item["text"]
                                        if (t is String) {
                                            if (sb.isNotEmpty()) sb.append("\n")
                                            sb.append(t)
                                        }
                                    }
                                }
                                if (sb.isNotEmpty()) return@withContext sb.toString()
                            }
                            val contStr = messageObj["content"]
                            if (contStr is String && contStr.isNotBlank()) return@withContext contStr
                        }
                    }
                }


                val firstChoice = if (choices is List<*>) choices.firstOrNull() else null
                if (firstChoice is Map<*, *>) {
                    val textVal = firstChoice["text"]
                    if (textVal is String) return@withContext textVal
                }

                return@withContext null
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val ctx = appContext ?: return true
        val cm = ctx.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager ?: return false
        val network = cm.activeNetwork ?: return false
        val caps = cm.getNetworkCapabilities(network) ?: return false
        return caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
