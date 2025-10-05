package vcmsa.projects.gameguessr

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.gameguessr.Adapters.GameAdapter
import vcmsa.projects.gameguessr.Class.Game
import vcmsa.projects.gameguessr.Database.DatabaseBuilder

class Encyclopedia : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameAdapter
    private lateinit var dao: vcmsa.projects.gameguessr.DAOs.GameDAO

    private var allGames = listOf<Game>()

    private val genreOptions = listOf("All", "Action", "Adventure", "RPG", "Shooter", "Simulation", "Sports", "Strategy", "Puzzle", "Horror", "Racing", "Other")
    private val platformOptions = listOf("All", "PC", "PS2", "PS3", "PS4", "PS5", "Xbox 360", "Xbox Series X", "Nintendo Switch", "Nintendo Switch 2")
    private val povOptions = listOf("All", "First-person", "Third-person", "Top-down", "Side-scroller", "Isometric")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encyclopedia)

        dao = DatabaseBuilder.getInstance(applicationContext).gameDao()

        recyclerView = findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GameAdapter(emptyList()) { selectedGame ->
            val intent = Intent(this, GameDetail::class.java)
            intent.putExtra("GAME_ID", selectedGame.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        findViewById<FloatingActionButton>(R.id.fabFilter).setOnClickListener {
            showFilterBottomSheet()
        }

        loadGames()
    }

    private fun showFilterBottomSheet() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_filter, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(dialogView)

        val spinnerGenre = dialogView.findViewById<Spinner>(R.id.spinnerGenre)
        val spinnerPlatform = dialogView.findViewById<Spinner>(R.id.spinnerPlatform)
        val spinnerPOV = dialogView.findViewById<Spinner>(R.id.spinnerPOV)
        val spinnerYear = dialogView.findViewById<Spinner>(R.id.spinnerYear)
        val buttonApply = dialogView.findViewById<Button>(R.id.btnApplyFilters)

        spinnerGenre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genreOptions)
        spinnerPlatform.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, platformOptions)
        spinnerPOV.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, povOptions)

        val years = mutableListOf("All") + allGames.map { it.releaseYear.toString() }.distinct().sortedDescending()
        spinnerYear.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, years)

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

    private fun loadGames() {
        CoroutineScope(Dispatchers.IO).launch {
            allGames = dao.getAllGames()
            runOnUiThread { adapter.updateGames(allGames) }
        }
    }

    private fun applyFilter(genre: String, platform: String, pov: String, year: String) {
        val filtered = allGames.filter { game ->
            val genreMatch = genre == "All" || game.genre.equals(genre, ignoreCase = true)
            val povMatch = pov == "All" || game.pov.equals(pov, ignoreCase = true)
            val platformMatch = platform == "All" || game.platform.any { it.equals(platform, ignoreCase = true) }
            val yearMatch = year == "All" || game.releaseYear.toString() == year
            genreMatch && povMatch && platformMatch && yearMatch
        }

        adapter.updateGames(filtered)
        Toast.makeText(this, "Showing ${filtered.size} result(s)", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadGames()
    }
}