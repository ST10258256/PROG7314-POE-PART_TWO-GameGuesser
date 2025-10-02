package vcmsa.projects.gameguessr.DAOs

import androidx.room.*
import vcmsa.projects.gameguessr.Class.Game

@Dao
interface GameDAO {
    // Insert a single game
    @Insert
    suspend fun insertGame(game: Game)

    // Insert multiple games at once
    @Insert
    suspend fun insertGames(games: List<Game>)

    // Update a game
    @Update
    suspend fun updateGame(game: Game)

    // Delete a game
    @Delete
    suspend fun deleteGame(game: Game)

    // Get all games
    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<Game>

    // Find a game by ID
    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: Int): Game?

    // Find games by genre
    @Query("SELECT * FROM games WHERE genre = :genre")
    suspend fun getGamesByGenre(genre: String): List<Game>
}