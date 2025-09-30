package com.example.gameguesser.ui.chatbot

data class ChatMessage(val sender: ChatMessage.Sender, val text: String) {
    enum class Sender { USER, BOT }
}
