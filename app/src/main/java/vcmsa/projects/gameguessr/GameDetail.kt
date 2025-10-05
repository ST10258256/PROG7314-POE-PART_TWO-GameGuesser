package vcmsa.projects.gameguessr

import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.gameguessr.Class.Game
import vcmsa.projects.gameguessr.Database.DatabaseBuilder

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

        val gameId = intent.getIntExtra("GAME_ID", -1)
        if (gameId == -1) {
            finish()
            return
        }

        val dao = DatabaseBuilder.getInstance(applicationContext).gameDao()

        CoroutineScope(Dispatchers.IO).launch {
            val game = dao.getGameById(gameId)
            game?.let {
                runOnUiThread {
                    textName.text = it.name
                    textGenre.text = "Genre: ${it.genre}"
                    textPlatform.text = "Platforms: ${it.platform.joinToString(", ")}"
                    textReleaseYear.text = "Release Year: ${it.releaseYear}"
                    textDeveloper.text = "Developer: ${it.developer}"
                    textPublisher.text = "Publisher: ${it.publisher}"
                    textBudget.text = "Budget: $${it.budget}"
                    textSaga.text = "Saga: ${it.saga}"
                    textPOV.text = "POV: ${it.pov}"
                    textDescription.text = it.description

                    if (it.coverImagePath.isNotEmpty()) {
                        Glide.with(this@GameDetail)
                            .load(it.coverImagePath)
                            .into(imageCover)
                    }
                }
            }
        }
    }
}