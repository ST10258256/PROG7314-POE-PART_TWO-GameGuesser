package com.example.gameguesser.ui.chatbot

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class OpenRouterMessageContent(val type: String = "text", val text: String)
data class OpenRouterMessage(val role: String, val content: List<OpenRouterMessageContent>)
data class OpenRouterRequest(val model: String, val messages: List<OpenRouterMessage>)

interface OpenRouterApi {
    @POST("chat/completions")
    suspend fun createChat(@Body body: OpenRouterRequest): Response<Map<String, Any>>
}
