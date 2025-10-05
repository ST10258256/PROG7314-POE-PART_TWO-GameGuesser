package com.example.gameguesser.data

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName

@Entity(tableName = "games")
@TypeConverters(GameConverters::class)
data class Game(

    @Ignore
    @SerializedName("_id")
    var _id: IdObject? = null,  // holds the Mongo _id

    @PrimaryKey
    var id: String = "",

    var name: String = "",
    var genre: String = "",
    var platforms: List<String> = emptyList(),
    var releaseYear: Int = 0,
    var developer: String = "",
    var publisher: String = "",
    var description: String = "",
    var coverImageUrl: String = "",
    var budget: Double = 0.0,
    var saga: String = "",
    var pov: String = "",
    var clues: List<String> = emptyList(),
    var keywords: List<String> = emptyList(),

    ) {

    val mongoId: String
        get() = _id?.oid ?: id

    constructor() : this(_id = null)
}

data class IdObject(
    @SerializedName("\$oid")
    val oid: String = ""
)
