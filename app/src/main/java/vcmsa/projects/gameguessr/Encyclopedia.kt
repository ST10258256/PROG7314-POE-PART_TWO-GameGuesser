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

    private lateinit var spinnerGenre: Spinner
    private lateinit var spinnerPlatform: Spinner
    private lateinit var spinnerPOV: Spinner
    private lateinit var spinnerYear: Spinner
    private lateinit var buttonApplyFilter: Button

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
            val intent = android.content.Intent(this, GameDetail::class.java)
            intent.putExtra("GAME_ID", selectedGame.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        spinnerGenre = findViewById(R.id.spinnerFilterGenre)
        spinnerPlatform = findViewById(R.id.spinnerFilterPlatform)
        spinnerPOV = findViewById(R.id.spinnerFilterPOV)
        spinnerYear = findViewById(R.id.spinnerFilterYear)
        buttonApplyFilter = findViewById(R.id.buttonApplyFilter)

        spinnerGenre.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genreOptions)
        spinnerPlatform.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, platformOptions)
        spinnerPOV.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, povOptions)

        buttonApplyFilter.setOnClickListener { applyFilter() }

        loadGames()
    }

    private fun loadGames() {
        CoroutineScope(Dispatchers.IO).launch {
            allGames = dao.getAllGames()
            val years = mutableListOf("All") + allGames.map { it.releaseYear.toString() }.distinct().sortedDescending()
            runOnUiThread {
                spinnerYear.adapter = ArrayAdapter(this@Encyclopedia, android.R.layout.simple_spinner_dropdown_item, years)
                adapter.updateGames(allGames)
            }
        }
    }

    private fun applyFilter() {
        val selectedGenre = spinnerGenre.selectedItem.toString()
        val selectedPlatform = spinnerPlatform.selectedItem.toString()
        val selectedPOV = spinnerPOV.selectedItem.toString()
        val selectedYear = spinnerYear.selectedItem.toString()

        val filtered = allGames.filter { game ->
            (selectedGenre == "Genre" || game.genre.equals(selectedGenre, ignoreCase = true)) &&
                    (selectedPOV == "POV" || game.pov.equals(selectedPOV, ignoreCase = true)) &&
                    (selectedPlatform == "Platform" || game.platform.any { it.equals(selectedPlatform, ignoreCase = true) }) &&
                    (selectedYear == "Platform" || game.releaseYear.toString() == selectedYear)
        }

        adapter.updateGames(filtered)

        Toast.makeText(this, "Showing ${filtered.size} result(s)", Toast.LENGTH_SHORT).show()
    }

    override fun onResume() {
        super.onResume()
        loadGames()
    }
}