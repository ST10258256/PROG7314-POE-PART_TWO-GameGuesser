package vcmsa.projects.gameguessr.Class

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class Game(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val Genre: String,
    val Platform: List<String>,
    val ReleaseYear: Int,
    val Developer: String,
    val Publisher: String,
    val Description: String,
    val CoverImage: String,
    val Budget: Double,
    val Saga: String,
    val POV: String,
    val Keywords: List<String>,
    val Clues: List<String>
)