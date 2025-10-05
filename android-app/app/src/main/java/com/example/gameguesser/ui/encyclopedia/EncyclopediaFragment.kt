package com.example.gameguesser.ui.encyclopedia

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameguesser.Adapters.GameAdapter
import com.example.gameguesser.Database.AppDatabase
import com.example.gameguesser.GameDetail
import com.example.gameguesser.R
import com.example.gameguesser.data.RetrofitClient
import com.example.gameguesser.data.Game
import com.example.gameguesser.repository.GameRepository
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class EncyclopediaFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameAdapter
    private var allGames = listOf<Game>()

    private val genreOptions = listOf("All", "Action", "Adventure", "RPG", "Shooter", "Simulation", "Sports", "Strategy", "Puzzle", "Horror", "Racing", "Other")
    private val platformOptions = listOf("All", "PC", "PS2", "PS3", "PS4", "PS5", "Xbox 360", "Xbox Series X", "Nintendo Switch")
    private val povOptions = listOf("All", "First-person", "Third-person", "Top-down", "Side-scroller", "Isometric")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_encyclopedia, container, false)

        recyclerView = view.findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = GameAdapter(allGames) { selectedGame ->
            Log.d("GameSelected", "Game: ${selectedGame.name}, id: ${selectedGame.id}")
            val intent = Intent(requireContext(), GameDetail::class.java)
            intent.putExtra("GAME_ID", selectedGame.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        view.findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
            showFilterBottomSheet()
        }

        fetchGames()

        return view
    }

    private fun fetchGames() {
        val dao = AppDatabase.getDatabase(requireContext()).gameDao()
        val repository = GameRepository(dao, RetrofitClient.api)

        lifecycleScope.launch {
            try {
                allGames = repository.getAllGames()
                adapter.updateGames(allGames)
                Log.d("GAME_FETCH", "Loaded ${allGames.size} games")
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Failed to load games", Toast.LENGTH_SHORT).show()
                Log.e("GAME_FETCH", "Error fetching games", e)
            }
        }
    }

    private fun showFilterBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filter, null)
        val dialog = BottomSheetDialog(requireContext())
        dialog.setContentView(dialogView)

        val spinnerGenre = dialogView.findViewById<Spinner>(R.id.spinnerGenre)
        val spinnerPlatform = dialogView.findViewById<Spinner>(R.id.spinnerPlatform)
        val spinnerPOV = dialogView.findViewById<Spinner>(R.id.spinnerPOV)
        val spinnerYear = dialogView.findViewById<Spinner>(R.id.spinnerYear)
        val buttonApply = dialogView.findViewById<Button>(R.id.btnApplyFilters)

        spinnerGenre.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, genreOptions)
        spinnerPlatform.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, platformOptions)
        spinnerPOV.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, povOptions)

        val years = mutableListOf("All") + allGames.map { it.releaseYear.toString() }.distinct().sortedDescending()
        spinnerYear.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, years)

        buttonApply.setOnClickListener {
            val selectedGenre = spinnerGenre.selectedItem.toString()
            val selectedPlatform = spinnerPlatform.selectedItem.toString()
            val selectedPOV = spinnerPOV.selectedItem.toString()
            val selectedYear = spinnerYear.selectedItem.toString()
            applyFilter(selectedGenre, selectedPlatform, selectedPOV, selectedYear)
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun applyFilter(genre: String, platform: String, pov: String, year: String) {
        val filtered = allGames.filter { game ->
            val genreMatch = genre == "All" || game.genre.equals(genre, ignoreCase = true)
            val povMatch = pov == "All" || game.pov.equals(pov, ignoreCase = true)
            val platformMatch = platform == "All" || game.platforms.any { it.equals(platform, ignoreCase = true) }
            val yearMatch = year == "All" || game.releaseYear.toString() == year
            genreMatch && povMatch && platformMatch && yearMatch
        }
        adapter.updateGames(filtered)
        Toast.makeText(requireContext(), "Showing ${filtered.size} result(s)", Toast.LENGTH_SHORT).show()
    }
}
