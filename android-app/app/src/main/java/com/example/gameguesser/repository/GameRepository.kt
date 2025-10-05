package com.example.gameguesser.repository

import com.example.gameguesser.DAOs.GameDAO.GameDao
import com.example.gameguesser.data.ApiService
import com.example.gameguesser.data.Game

class GameRepository(
    private val dao: GameDao,
    private val api: ApiService
) {

    // Fetch all games: online first, fallback to local
    suspend fun getAllGames(): List<Game> {
        return try {
            val response = api.getAllGamesFull().execute()
            val games = response.body() ?: emptyList()
            if (games.isNotEmpty()) {
                dao.insertGames(games)  // update local cache
            }
            games.ifEmpty { dao.getAllGames() } // fallback if empty
        } catch (e: Exception) {
            dao.getAllGames() // offline fallback
        }
    }

    // Fetch single game by ID: online first, fallback to local
    suspend fun getGameById(id: String): Game? {
        return try {
            val response = api.getGameById(id).execute()
            val game = response.body()
            game?.let { dao.insertGame(it) } // cache locally
            game ?: dao.getGameById(id)
        } catch (e: Exception) {
            dao.getGameById(id)
        }
    }

    // Optional: fetch a random game for Keyword game
    suspend fun getRandomGame(): Game? {
        val localGames = dao.getAllGames()
        return if (localGames.isNotEmpty()) {
            localGames.random()
        } else {
            try {
                val response = api.getRandomGame().execute()
                val game = response.body()?.let {
                    Game(
                        id = it.id,
                        name = it.name,
                        keywords = it.keywords
                    )
                }
                game?.let { dao.insertGame(it) }
                game
            } catch (e: Exception) {
                null
            }
        }
    }

    // Force sync local DB from API
    suspend fun syncFromApi() {
        try {
            val response = api.getAllGamesFull().execute()
            val games = response.body() ?: emptyList()
            if (games.isNotEmpty()) {
                dao.insertGames(games)
            }
        } catch (_: Exception) { /* ignore errors */ }
    }
}



