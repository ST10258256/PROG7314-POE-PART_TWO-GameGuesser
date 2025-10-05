package com.example.gameguesser.models

data class RandomGameResponse(
    val id: String,
    val name: String,
    val keywords: List<String>,
    val coverImageUrl: String?
)