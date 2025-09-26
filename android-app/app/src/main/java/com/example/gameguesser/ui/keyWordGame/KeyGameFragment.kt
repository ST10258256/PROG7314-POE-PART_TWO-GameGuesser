package com.example.gameguesser.ui.keyWordGame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.gameguesser.R
import com.example.gameguesser.data.RetrofitClient
import com.example.gameguesser.models.GuessResponse
import com.example.gameguesser.models.RandomGameResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class KeyGameFragment : Fragment() {

    private var currentGameId: Int? = null

    private lateinit var keywordsText: TextView
    private lateinit var resultText: TextView
    private lateinit var guessInput: EditText
    private lateinit var guessButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_key_game, container, false)

        keywordsText = view.findViewById(R.id.keywordsText)
        resultText = view.findViewById(R.id.resultText)
        guessInput = view.findViewById(R.id.guessInput)
        guessButton = view.findViewById(R.id.guessButton)

        fetchRandomGame()

        guessButton.setOnClickListener {
            val guess = guessInput.text.toString()
            currentGameId?.let { id ->
                submitGuess(id, guess)
            } ?: run {
                Toast.makeText(requireContext(), "Game not loaded yet", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun fetchRandomGame() {
        RetrofitClient.api.getRandomGame().enqueue(object : Callback<RandomGameResponse> {
            override fun onResponse(
                call: Call<RandomGameResponse>,
                response: Response<RandomGameResponse>
            ) {
                if (response.isSuccessful) {
                    val game = response.body()
                    game?.let {
                        currentGameId = it.id
                        keywordsText.text = it.keywords.joinToString(", ")
                        resultText.text = ""
                        guessInput.text.clear()
                    }
                } else {
                    keywordsText.text = "Failed to load game"
                }
            }

            override fun onFailure(call: Call<RandomGameResponse>, t: Throwable) {
                keywordsText.text = "Error: ${t.message}"
            }
        })
    }

    private fun submitGuess(gameId: Int, guess: String) {
        RetrofitClient.api.submitGuess(gameId, guess).enqueue(object : Callback<GuessResponse> {
            override fun onResponse(call: Call<GuessResponse>, response: Response<GuessResponse>) {
                val result = response.body()
                if (result != null) {
                    if (result.correct) {
                        resultText.text = result.message ?: "Correct"
                        Toast.makeText(requireContext(), "Correct", Toast.LENGTH_SHORT).show()
                    } else {
                        resultText.text = "Wrong--> Hint: ${result.hint}"
                        Toast.makeText(requireContext(), "Hint: ${result.hint}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    resultText.text = "Error: Invalid response"
                }
            }

            override fun onFailure(call: Call<GuessResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
