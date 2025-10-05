package vcmsa.projects.gameguessr

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val addButton = findViewById<Button>(R.id.button)
        addButton.setOnClickListener {
            val intent = Intent(this, AddGame::class.java)
            startActivity(intent)
        }

        val encyclopediaButton = findViewById<Button>(R.id.button2)
        encyclopediaButton.setOnClickListener {
            val intent = Intent(this, Encyclopedia::class.java)
            startActivity(intent)
        }
    }
}