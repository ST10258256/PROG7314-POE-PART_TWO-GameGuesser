package com.example.gameguesser.ui.keyWordGame

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.gameguesser.Database.AppDatabase
import com.example.gameguesser.R
import com.example.gameguesser.data.RetrofitClient
import com.example.gameguesser.repository.GameRepository
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class KeyGameFragment : Fragment() {

    private var currentGameId: String? = null
    private var currentGameName: String? = null
    private var currentGameCover: String? = null

    private lateinit var resultText: TextView
    private lateinit var guessInput: AutoCompleteTextView
    private lateinit var guessButton: Button
    private lateinit var keywordsChipGroup: ChipGroup
    private lateinit var heartsContainer: LinearLayout
    private var hearts: MutableList<ImageView> = mutableListOf()
    private val maxLives = 5

    private lateinit var adapter: ArrayAdapter<String>
    private var allGames: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_key_game, container, false)

        resultText = view.findViewById(R.id.resultText)
        guessInput = view.findViewById(R.id.guessInput)
        guessButton = view.findViewById(R.id.guessButton)
        keywordsChipGroup = view.findViewById(R.id.keywordsChipGroup)
        heartsContainer = view.findViewById(R.id.guessHeartsContainer)

        adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, allGames)
        guessInput.setAdapter(adapter)

        // Initialize hearts
        hearts.clear()
        for (i in 0 until maxLives) {
            val heart = heartsContainer.getChildAt(i) as ImageView
            hearts.add(heart)
        }

        fetchAllGames()
        fetchRandomGame()

        guessInput.addTextChangedListener { editable ->
            val input = editable.toString()
            val filtered = allGames.filter { it.contains(input, ignoreCase = true) }
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, filtered)
            guessInput.setAdapter(adapter)
            adapter.notifyDataSetChanged()
            guessInput.showDropDown()
        }

        guessButton.setOnClickListener {
            val guess = guessInput.text.toString()
            currentGameId?.let { id -> submitGuess(id, guess) }
                ?: Toast.makeText(requireContext(), "Game not loaded yet", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun fetchAllGames() {
        val dao = AppDatabase.getDatabase(requireContext()).gameDao()
        val repository = GameRepository(dao, RetrofitClient.api)

        CoroutineScope(Dispatchers.IO).launch {
            val games = repository.getAllGames().map { it.name }
            allGames.clear()
            allGames.addAll(games)
            CoroutineScope(Dispatchers.Main).launch { adapter.notifyDataSetChanged() }
        }
    }

    private fun fetchRandomGame() {
        val dao = AppDatabase.getDatabase(requireContext()).gameDao()
        val repository = GameRepository(dao, RetrofitClient.api)

        CoroutineScope(Dispatchers.IO).launch {
            val game = repository.getRandomGame()
            game?.let {
                currentGameId = it.id
                currentGameName = it.name
                currentGameCover = it.coverImageUrl

                CoroutineScope(Dispatchers.Main).launch {
                    keywordsChipGroup.removeAllViews()
                    it.keywords.forEach { keyword -> addChip(keyword) }
                    resultText.text = ""
                    guessInput.text.clear()
                    resetHearts()
                }
            }
        }
    }

    private fun submitGuess(gameId: String, guess: String) {
        // Keep API call for submitting guess, unchanged
        RetrofitClient.api.submitGuess(gameId, guess).enqueue(object : retrofit2.Callback<com.example.gameguesser.models.GuessResponse> {
            override fun onResponse(call: retrofit2.Call<com.example.gameguesser.models.GuessResponse>, response: retrofit2.Response<com.example.gameguesser.models.GuessResponse>) {
                val result = response.body()
                if (result != null) {
                    if (result.correct) {
                        showEndGameDialog(true, currentGameName ?: "Unknown", currentGameCover)
                    } else {
                        if (hearts.isNotEmpty()) {
                            val lastHeart = hearts.removeAt(hearts.size - 1)
                            lastHeart.visibility = View.INVISIBLE
                        }
                        addChip(result.hint ?: "No hint")
                        resultText.text = "Wrong"
                        if (hearts.isEmpty()) {
                            showEndGameDialog(false, currentGameName ?: "Unknown", currentGameCover)
                            guessButton.isEnabled = false
                            guessInput.isEnabled = false
                        } else {
                            Toast.makeText(requireContext(), "Hint: ${result.hint ?: "No hint"}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    resultText.text = "Error: Invalid response"
                }
            }

            override fun onFailure(call: retrofit2.Call<com.example.gameguesser.models.GuessResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun resetHearts() {
        hearts.clear()
        for (i in 0 until maxLives) {
            val heart = heartsContainer.getChildAt(i) as ImageView
            heart.visibility = View.VISIBLE
            hearts.add(heart)
        }
        guessButton.isEnabled = true
        guessInput.isEnabled = true
    }

    private fun addChip(text: String) {
        val chip = Chip(requireContext())
        chip.text = text
        chip.isClickable = false
        chip.isCheckable = false
        chip.setTextColor(resources.getColor(android.R.color.white, null))
        chip.chipBackgroundColor = resources.getColorStateList(android.R.color.holo_purple, null)
        keywordsChipGroup.addView(chip)
    }

    private fun showEndGameDialog(won: Boolean, gameName: String, coverUrl: String?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_end_game, null)
        val imageView = dialogView.findViewById<ImageView>(R.id.gameCoverImage)
        val titleText = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val nameText = dialogView.findViewById<TextView>(R.id.gameName)
        val playAgainBtn = dialogView.findViewById<Button>(R.id.playAgainButton)
        val mainMenuBtn = dialogView.findViewById<Button>(R.id.mainMenuButton)

        titleText.text = if (won) "Congratulations!" else "Better luck next time"
        nameText.text = "The game was: $gameName"

        coverUrl?.let {
            Glide.with(this)
                .load(it)
                .into(imageView)
        }

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setCancelable(false)
            .create()

        playAgainBtn.setOnClickListener {
            dialog.dismiss()
            resetGame()
        }

        mainMenuBtn.setOnClickListener {
            dialog.dismiss()
            requireActivity().onBackPressed()
        }

        dialog.show()
    }

    private fun resetGame() {
        keywordsChipGroup.removeAllViews()
        resetHearts()
        guessInput.text.clear()
        resultText.text = ""
        fetchRandomGame()
    }
}
