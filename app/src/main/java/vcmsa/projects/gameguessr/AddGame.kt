package vcmsa.projects.gameguessr

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.gameguessr.Class.Game
import vcmsa.projects.gameguessr.Database.DatabaseBuilder

class AddGame : AppCompatActivity() {
    private var selectedImageUri: Uri? = null

    private lateinit var imagePreview: ImageView
    private lateinit var coverPathInput: EditText

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val uri: Uri? = result.data?.data
                if (uri != null) {
                    selectedImageUri = uri
                    Glide.with(this).load(uri).into(imagePreview)
                    coverPathInput.setText(uri.toString())
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_game)

        val nameInput = findViewById<EditText>(R.id.editTextName)
        val genreInput = findViewById<EditText>(R.id.editTextGenre)
        val platformInput = findViewById<EditText>(R.id.editTextPlatform)
        val releaseYearInput = findViewById<EditText>(R.id.editTextReleaseYear)
        val developerInput = findViewById<EditText>(R.id.editTextDeveloper)
        val publisherInput = findViewById<EditText>(R.id.editTextPublisher)
        val descriptionInput = findViewById<EditText>(R.id.editTextDescription)
        coverPathInput = findViewById(R.id.editTextCoverImagePath)
        val budgetInput = findViewById<EditText>(R.id.editTextBudget)
        val sagaInput = findViewById<EditText>(R.id.editTextSaga)
        val povInput = findViewById<EditText>(R.id.editTextPOV)

        imagePreview = findViewById(R.id.imagePreview)

        val clue1 = findViewById<EditText>(R.id.editTextClue1)
        val clue2 = findViewById<EditText>(R.id.editTextClue2)
        val clue3 = findViewById<EditText>(R.id.editTextClue3)
        val clue4 = findViewById<EditText>(R.id.editTextClue4)
        val clue5 = findViewById<EditText>(R.id.editTextClue5)

        val saveButton = findViewById<Button>(R.id.buttonSave)

        val gameDao = DatabaseBuilder.getInstance(applicationContext).gameDao()

        coverPathInput.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            pickImageLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            val name = nameInput.text.toString().trim()
            val genre = genreInput.text.toString().trim()
            val platform = platformInput.text.toString()
                .split(",")
                .map { it.trim() }
                .filter { it.isNotEmpty() }

            val releaseYear = releaseYearInput.text.toString().toIntOrNull() ?: 0
            val developer = developerInput.text.toString().trim()
            val publisher = publisherInput.text.toString().trim()
            val description = descriptionInput.text.toString().trim()
            val coverImagePath = selectedImageUri?.toString() ?: ""
            val budget = budgetInput.text.toString().toDoubleOrNull() ?: 0.0
            val saga = sagaInput.text.toString().trim()
            val pov = povInput.text.toString().trim()

            val clues = listOf(
                clue1.text.toString().trim(),
                clue2.text.toString().trim(),
                clue3.text.toString().trim()
            ).filter { it.isNotEmpty() } + listOf(
                clue4.text.toString().trim(),
                clue5.text.toString().trim()
            ).filter { it.isNotEmpty() }

            if (name.isEmpty() || genre.isEmpty()) {
                Toast.makeText(this, "Name and Genre are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (clues.size < 5) {
                Toast.makeText(this, "Please enter 5 clues", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (coverImagePath.isEmpty()) {
                Toast.makeText(this, "Please pick a cover image", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            //Insert into Room DB
            CoroutineScope(Dispatchers.IO).launch {
                val game = Game(
                    name = name,
                    genre = genre,
                    platform = platform,
                    releaseYear = releaseYear,
                    developer = developer,
                    publisher = publisher,
                    description = description,
                    coverImagePath = coverImagePath,
                    budget = budget,
                    saga = saga,
                    pov = pov,
                    clues = clues
                )
                gameDao.insertGame(game)//dao function to insert the game

                launch(Dispatchers.Main) {
                    Toast.makeText(
                        this@AddGame,
                        "Game saved!",
                        Toast.LENGTH_SHORT
                    ).show()

                    val intent = Intent(this@AddGame, Encyclopedia::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }
    }
}