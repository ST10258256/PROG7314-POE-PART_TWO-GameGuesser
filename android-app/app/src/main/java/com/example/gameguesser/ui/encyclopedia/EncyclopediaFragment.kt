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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameguesser.R
import com.example.gameguesser.Adapters.GameAdapter
import com.example.gameguesser.GameDetail
import com.example.gameguesser.data.Game
import com.example.gameguesser.data.RetrofitClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            Log.d("GameSelected", "Game: ${selectedGame.name}, mongoId: ${selectedGame.mongoId}")
            val intent = Intent(requireContext(), GameDetail::class.java)
            intent.putExtra("GAME_ID", selectedGame.mongoId)
            startActivity(intent)
        }

        recyclerView.adapter = adapter


        view.findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
            showFilterBottomSheet()
        }

        fetchGamesFromApi()

        return view
    }

    private fun fetchGamesFromApi() {
        RetrofitClient.api.getAllGamesFull().enqueue(object : Callback<List<Game>> {
            override fun onResponse(call: Call<List<Game>>, response: Response<List<Game>>) {
                if (response.isSuccessful && response.body() != null) {
                    allGames = response.body()!!
                    allGames.forEach { game ->
                        Log.d("GAME_FETCH", "Game: ${game.name}, id: ${game.id}, mongoId: ${game._id?.oid}")
                    }
                    adapter.updateGames(allGames)

                } else {
                    Toast.makeText(requireContext(), "Failed to load games", Toast.LENGTH_SHORT).show()
                    Log.e("GAME_FETCH", "Response not successful or empty: ${response.errorBody()?.string()}")

                }
            }

            override fun onFailure(call: Call<List<Game>>, t: Throwable) {
                Toast.makeText(requireContext(), "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("GAME_FETCH", "API call failed", t)
            }
        })
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
