package com.example.gameguesser.ui.chatbot

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*

data class ChatMessage(val sender: String, val text: String)

class ChatbotViewModel : ViewModel() {
    private val _messages = MutableLiveData<List<ChatMessage>>(emptyList())
    val messages: LiveData<List<ChatMessage>> = _messages

    private val viewModelJob = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun sendMessage(text: String) {
        // append user message
        val current = _messages.value ?: emptyList()
        _messages.value = current + ChatMessage("You", text)

        // simple simulated bot reply (replace with real API call)
        scope.launch {
            delay(500) // simulate thinking
            val reply = "Echo: $text"
            _messages.value = (_messages.value ?: emptyList()) + ChatMessage("Bot", reply)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
