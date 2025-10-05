package com.example.gameguesser.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "games")
//roomdb does not support List<String> directly so,
//we have convert into a storable type using a TypeConverter
@TypeConverters(GameConverters::class)
data class Game(
    val id: String,
    val name: String,
    val genre: String,
    val platforms: List<String>,
    val releaseYear: Int,
    val developer: String,
    val publisher: String,
    val description: String,
    val coverImageUrl: String,
    val budget: Double,//yet to be change to string
    val saga: String,
    val pov: String,
    val keywords: List<String> = emptyList() , //not doing anything with keywords for now so i made it a default value
    val clues: List<String>
)
