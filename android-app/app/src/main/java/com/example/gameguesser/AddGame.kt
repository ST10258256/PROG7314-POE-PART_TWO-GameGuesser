//package com.example.gameguesser
//
//import android.app.Activity
//import android.app.AlertDialog
//import android.app.DatePickerDialog
//import android.content.Intent
//import android.net.Uri
//import android.os.Bundle
//import android.view.View
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.EditText
//import android.widget.ImageView
//import android.widget.Spinner
//import android.widget.TextView
//import android.widget.Toast
//import androidx.activity.enableEdgeToEdge
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.view.ViewCompat
//import androidx.core.view.WindowInsetsCompat
//import com.bumptech.glide.Glide
//import com.example.gameguesser.Database.DatabaseBuilder.DatabaseBuilder
//import com.example.gameguesser.data.Game
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import java.util.Calendar
//
//class AddGame : AppCompatActivity() {
//    private val genreOptions = arrayOf("Action", "Adventure", "RPG", "Shooter", "Simulation",
//        "Sports", "Strategy", "Puzzle", "Horror", "Racing")
//
//    private val povOptions = arrayOf("First-person", "Third-person", "Top-down", "Side-scroller", "Isometric")
//
//    private val platformOptions = arrayOf("PC", "PS4", "PS5", "PS3", "PS2", "Xbox 360", "Xbox Series X", "Nintendo Switch", "Nintendo Switch 2")
//    private val selectedPlatforms = mutableListOf<String>()
//
//    private var selectedImageUri: Uri? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_add_game)
//
//        val editName = findViewById<EditText>(R.id.editTextName)
//        val spinnerGenre = findViewById<Spinner>(R.id.spinnerGenre)
//        val buttonSelectPlatforms = findViewById<Button>(R.id.buttonSelectPlatforms)
//        val textSelectedPlatforms = findViewById<TextView>(R.id.textViewSelectedPlatforms)
//        val editReleaseYear = findViewById<EditText>(R.id.editTextReleaseYear)
//        val editDeveloper = findViewById<EditText>(R.id.editTextDeveloper)
//        val editPublisher = findViewById<EditText>(R.id.editTextPublisher)
//        val editDescription = findViewById<EditText>(R.id.editTextDescription)
//        val editBudget = findViewById<EditText>(R.id.editTextBudget)
//        val editSaga = findViewById<EditText>(R.id.editTextSaga)
//        val spinnerPOV = findViewById<Spinner>(R.id.spinnerPOV)
//        val editClue1 = findViewById<EditText>(R.id.editTextClue1)
//        val editClue2 = findViewById<EditText>(R.id.editTextClue2)
//        val editClue3 = findViewById<EditText>(R.id.editTextClue3)
//        val editClue4 = findViewById<EditText>(R.id.editTextClue4)
//        val editClue5 = findViewById<EditText>(R.id.editTextClue5)
//        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
//        val editCoverImagePath = findViewById<EditText>(R.id.editTextCoverImagePath)
//        val buttonSave = findViewById<Button>(R.id.buttonSave)
//
//        spinnerGenre.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genreOptions)
//        spinnerPOV.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, povOptions)
//
//        buttonSelectPlatforms.setOnClickListener {
//            val checkedItems = BooleanArray(platformOptions.size) { i -> selectedPlatforms.contains(platformOptions[i]) }
//            AlertDialog.Builder(this)
//                .setTitle("Select Platforms")
//                .setMultiChoiceItems(platformOptions, checkedItems) { _, which, isChecked ->
//                    if (isChecked) selectedPlatforms.add(platformOptions[which])
//                    else selectedPlatforms.remove(platformOptions[which])
//                }
//                .setPositiveButton("OK") { _, _ ->
//                    textSelectedPlatforms.text = if (selectedPlatforms.isEmpty()) "No platforms selected"
//                    else selectedPlatforms.joinToString(", ")
//                }
//                .setNegativeButton("Cancel", null)
//                .show()
//        }
//
//        editReleaseYear.setOnClickListener {
//            val calendar = Calendar.getInstance()
//            val year = calendar.get(Calendar.YEAR)
//            val month = calendar.get(Calendar.MONTH)
//            val day = calendar.get(Calendar.DAY_OF_MONTH)
//
//            DatePickerDialog(this, { _, selectedYear, _, _ ->
//                editReleaseYear.setText(selectedYear.toString())
//            }, year, month, day).show()
//        }
//
//        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
//            uri?.let {
//                selectedImageUri = it
//                Glide.with(this).load(it).into(imagePreview)
//                editCoverImagePath.setText(it.toString())
//            }
//        }
//
//        editCoverImagePath.setOnClickListener {
//            imagePicker.launch("image/*")
//        }
//
//        buttonSave.setOnClickListener {
//            val name = editName.text.toString()
//            if (name.isBlank()) {
//                Toast.makeText(this, "Please enter the game name", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val game = Game(
//                name = name,
//                genre = spinnerGenre.selectedItem.toString(),
//                platforms = selectedPlatforms.toList(),
//                releaseYear = editReleaseYear.text.toString().toIntOrNull() ?: 0,
//                developer = editDeveloper.text.toString(),
//                publisher = editPublisher.text.toString(),
//                description = editDescription.text.toString(),
//                coverImageUrl = selectedImageUri?.toString() ?: "",
//                budget = editBudget.text.toString().toDoubleOrNull() ?: 0.0,
//                saga = editSaga.text.toString(),
//                pov = spinnerPOV.selectedItem.toString(),
//                clues = listOf(
//                    editClue1.text.toString(),
//                    editClue2.text.toString(),
//                    editClue3.text.toString(),
//                    editClue4.text.toString(),
//                    editClue5.text.toString()
//                ),
//                keywords = emptyList()
//            )
//
//            val dao = DatabaseBuilder.getInstance(applicationContext).gameDao()
//            CoroutineScope(Dispatchers.IO).launch {
//                dao.insertGame(game)
//                runOnUiThread {
//                    Toast.makeText(this@AddGame, "Game saved!", Toast.LENGTH_SHORT).show()
//                    //val intent = Intent(this@AddGame, Encyclopedia::class.java)
//                    startActivity(intent)
//                    finish()
//                }
//            }
//        }
//    }
//}