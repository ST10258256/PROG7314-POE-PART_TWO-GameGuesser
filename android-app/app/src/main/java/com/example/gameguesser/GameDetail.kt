package com.example.gameguesser

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.gameguesser.data.Game
import com.example.gameguesser.data.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GameDetail : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game_detail)

        val imageCover = findViewById<ImageView>(R.id.imageViewCover)
        val textName = findViewById<TextView>(R.id.textViewName)
        val textGenre = findViewById<TextView>(R.id.textViewGenre)
        val textPlatform = findViewById<TextView>(R.id.textViewPlatform)
        val textReleaseYear = findViewById<TextView>(R.id.textViewReleaseYear)
        val textDeveloper = findViewById<TextView>(R.id.textViewDeveloper)
        val textPublisher = findViewById<TextView>(R.id.textViewPublisher)
        val textBudget = findViewById<TextView>(R.id.textViewBudget)
        val textSaga = findViewById<TextView>(R.id.textViewSaga)
        val textPOV = findViewById<TextView>(R.id.textViewPOV)
        val textDescription = findViewById<TextView>(R.id.textViewDescription)


        val gameId = intent.getStringExtra("GAME_ID")
        Log.d("GAME_DETAIL", "Received GAME_ID: $gameId")
        if (gameId.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid game ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

// Fetch game from API
        RetrofitClient.api.getGameById(gameId).enqueue(object : Callback<Game> {
            override fun onResponse(call: Call<Game>, response: Response<Game>) {
                val game = response.body()
                if (game != null) {
                    textName.text = game.name
                    textGenre.text = "Genre: ${game.genre}"
                    textPlatform.text = "Platforms: ${game.platforms.joinToString(", ")}"
                    textReleaseYear.text = "Release Year: ${game.releaseYear}"
                    textDeveloper.text = "Developer: ${game.developer}"
                    textPublisher.text = "Publisher: ${game.publisher}"
                    textBudget.text = "Budget: $${game.budget}"
                    textSaga.text = "Saga: ${game.saga}"
                    textPOV.text = "POV: ${game.pov}"
                    textDescription.text = game.description

                    Log.d("GAME_DETAIL", "Fetched game: ${game.name}, id: ${game.id}")

                    if (!game.coverImageUrl.isNullOrEmpty()) {
                        Glide.with(this@GameDetail)
                            .load(game.coverImageUrl)
                            .into(imageCover)
                    }
                } else {
                    Toast.makeText(this@GameDetail, "Game not found", Toast.LENGTH_SHORT).show()
                    Log.d("GAME_DETAIL", "Game not found for ID: $gameId")
                    finish()
                }
            }

            override fun onFailure(call: Call<Game>, t: Throwable) {
                Toast.makeText(this@GameDetail, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                finish()
            }
        })

    }
}
