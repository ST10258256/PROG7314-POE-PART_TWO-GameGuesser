package vcmsa.projects.gameguessr.Class

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "games")
//roomdb does not support List<String> directly so,
//we have convert into a storable type using a TypeConverter
@TypeConverters(GameConverters::class)
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val genre: String,
    val platform: List<String>,
    val releaseYear: Int,
    val developer: String,
    val publisher: String,
    val description: String,
    val coverImagePath: String,
    val budget: Double,
    val saga: String,
    val pov: String,
    val keywords: List<String> = emptyList() ,
    val clues: List<String>
)
