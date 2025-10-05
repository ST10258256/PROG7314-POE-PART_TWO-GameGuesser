package com.example.gameguesser.Class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val userId: String, // Google user ID
    val userName: String,           // Display name
    val streak: Int = 0             // Initial streak
)
