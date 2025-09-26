package com.example.gameguesser.models

data class GuessResponse(
    val correct: Boolean,
    val message: String? = null,
    val hint: String? = null
)