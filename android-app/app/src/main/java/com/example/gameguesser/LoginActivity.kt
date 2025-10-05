package com.example.gameguesser

import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gameguesser.Class.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.launch
import java.util.logging.Handler

class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var signInLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // setup for sign in
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()  // only setup for basic email
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // checks if they have already signed in
        val account = GoogleSignIn.getLastSignedInAccount(this)

        //used this to test the offline mode, works
        //val account: GoogleSignInAccount? = null

        if (account != null) {
            // User still has a valid Google session
            goToMainActivity(account)
        } else {
            // check SharedPreferences for offline mode
            val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
            val savedUserId = prefs.getString("userId", null)
            val savedUserName = prefs.getString("userName", null)

            if (savedUserId != null) {
                // User logged in before, allow offline access
                Toast.makeText(this, "Welcome back $savedUserName (offline)", Toast.LENGTH_SHORT).show()
                android.os.Handler(Looper.getMainLooper()).postDelayed({
                    goToMainActivity(null)
                }, 500)}
        }

        val btnGoogleSignIn = findViewById<SignInButton>(R.id.btnGoogleSignIn)
        btnGoogleSignIn.setOnClickListener {
            signIn() // runs the sign if if not already signed in
        }

        signInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleSignInResult(task)
        }
    }

    private fun signIn() {
        // gt the intent from the Google client and launch the sign-in flow
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                // saves them to shared pred for offline
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                with(prefs.edit()) {
                    putString("userName", account.displayName)
                    putString("userEmail", account.email)
                    putString("userId", account.id)
                    apply()
                }
                // also saves them to room
                val user = User(
                    userId = account.id ?: "",
                    userName = account.displayName ?: "Player",
                    streak = 0
                )

                val db = UserDatabase.getDatabase(this)
                kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
                    db.userDao().addUser(user)
                }

                // welcm back msg
                Toast.makeText(this, "Welcome ${account.displayName}", Toast.LENGTH_SHORT).show()

                // goes to main
                goToMainActivity(account)
            }
        } catch (e: ApiException) {
            Toast.makeText(this, "Sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }


    private fun goToMainActivity(account: GoogleSignInAccount?) {
        // flow to main, cant go back to login, needs to logout, ps. added in logout
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}

