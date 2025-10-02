package vcmsa.projects.gameguessr

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.gameguessr.Adapters.GameAdapter
import vcmsa.projects.gameguessr.Class.Game
import vcmsa.projects.gameguessr.DAOs.GameDAO
import vcmsa.projects.gameguessr.Database.DatabaseBuilder

class Encyclopedia : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GameAdapter
    private val gameDao by lazy { DatabaseBuilder.getInstance(applicationContext).gameDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_encyclopedia)

        recyclerView = findViewById(R.id.recyclerViewGames)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = GameAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadGames()
    }

    //auto-refreshes every time to get the latest games
    private fun loadGames() {
        CoroutineScope(Dispatchers.IO).launch {
            val games: List<Game> = gameDao.getAllGames()
            launch(Dispatchers.Main) {
                adapter.updateGames(games)
            }
        }
    }
}