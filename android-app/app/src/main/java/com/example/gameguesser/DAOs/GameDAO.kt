package com.example.gameguesser.DAOs.GameDAO

import androidx.room.*
import com.example.gameguesser.data.Game

@Dao
interface GameDao {

    @Query("SELECT * FROM games")
    suspend fun getAllGames(): List<Game>

    @Query("SELECT * FROM games WHERE id = :id")
    suspend fun getGameById(id: String): Game?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGames(games: List<Game>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGame(game: Game)
}



