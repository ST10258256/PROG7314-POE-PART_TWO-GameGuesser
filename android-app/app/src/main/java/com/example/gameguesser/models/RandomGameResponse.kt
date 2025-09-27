package com.example.gameguesser.models

data class RandomGameResponse(
    val id: Int,
    val name: String,
    val keywords: List<String>,
    val coverImageUrl: String?
)