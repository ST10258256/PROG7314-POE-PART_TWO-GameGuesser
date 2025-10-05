package com.example.gameguesser.data

import com.example.gameguesser.models.GuessResponse
import com.example.gameguesser.models.RandomGameResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiService {
    @GET("api/Games/random")
    fun getRandomGame(): Call<RandomGameResponse>

    @POST("api/Games/guess")
    fun submitGuess(
        @Query("gameId") gameId: Int,
        @Query("guess") guess: String
    ): Call<GuessResponse>

    @GET("api/games")
    fun getAllGames(): Call<List<String>>

    @GET("api/games/full")
    fun getAllGamesFull(): Call<List<Game>>

    @GET("api/games/{id}")
    fun getGameById(@Path("id") gameId: Int): Call<Game>

}