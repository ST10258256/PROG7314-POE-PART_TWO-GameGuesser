package com.example.gameguesser.repository

import android.util.Log
import com.example.gameguesser.DAOs.GameDAO.GameDao
import com.example.gameguesser.data.ApiService
import com.example.gameguesser.data.Game
import com.example.gameguesser.data.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.Result

class GameRepository(private val gameDao: GameDao) {

    private val api: ApiService = RetrofitClient.api

    // scope for Room IO work - tied to repository lifetime
    private val job = SupervisorJob()
    private val ioScope = CoroutineScope(Dispatchers.IO + job)

    fun clear() {
        // call this if you want to cancel any outstanding DB work when repository is disposed
        job.cancel()
    }

    fun fetchRandomGame(callback: (Result<com.example.gameguesser.models.RandomGameResponse?>) -> Unit) {
        api.getRandomGame().enqueue(object : Callback<com.example.gameguesser.models.RandomGameResponse> {
            override fun onResponse(
                call: Call<com.example.gameguesser.models.RandomGameResponse>,
                response: Response<com.example.gameguesser.models.RandomGameResponse>
            ) {
                if (response.isSuccessful) {
                    callback(Result.success(response.body()))
                } else {
                    callback(Result.failure(Exception("Random game response not successful: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<com.example.gameguesser.models.RandomGameResponse>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun fetchGameById(id: String, callback: (Result<Game?>) -> Unit) {
        api.getGameById(id).enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                if (response.isSuccessful) {
                    val game = response.body()
                    // cache locally if present — perform Room write on IO coroutine
                    if (game != null) {
                        ioScope.launch {
                            try {
                                gameDao.insertGame(game)
                            } catch (e: Exception) {
                                Log.w("GameRepository", "DB insert failed: ${e.message}")
                            }
                        }
                    }
                    callback(Result.success(game))
                } else {
                    callback(Result.failure(Exception("getGameById failed: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    fun fetchAllGamesFull(callback: (Result<List<Game>>) -> Unit) {
        api.getAllGamesFull().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful) {
                    val list = response.body() ?: emptyList()
                    // cache into DB on IO coroutine
                    ioScope.launch {
                        try {
                            gameDao.insertGames(list)
                        } catch (e: Exception) {
                            Log.w("GameRepository", "insertGames failed: ${e.message}")
                        }
                    }
                    callback(Result.success(list))
                } else {
                    callback(Result.failure(Exception("getAllGamesFull error: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                callback(Result.failure(t))
            }
        })
    }

    // fallback local DB lookup by name (suspend remains ok)
    suspend fun findGameByNameLocal(name: String): Game? {
        val all = gameDao.getAllGames()
        return all.firstOrNull { it.name.equals(name, ignoreCase = true) }
    }
}
