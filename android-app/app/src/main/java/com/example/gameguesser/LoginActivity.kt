package com.example.gameguesser

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

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

        // cheks if they havealready signed in
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            // wont show the signin if so
            goToMainActivity(account)
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
        // Get the intent from the Google client and launch the sign-in flow
        val signInIntent = googleSignInClient.signInIntent
        signInLauncher.launch(signInIntent)
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            // if fails, example sha-1 not added to ggogle console
            val account = completedTask.getResult(ApiException::class.java)
            if (account != null) {
                // shared pref for other parts when we need
                val prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                with(prefs.edit()) {
                    putString("userName", account.displayName)
                    putString("userEmail", account.email)
                    putString("userId", account.id)
                    apply()
                }

                // welcome back toast, could remove since we have the dynMIC label
                Toast.makeText(this, "Welcome ${account.displayName}", Toast.LENGTH_SHORT).show()

                // goes to main
                goToMainActivity(account)
            }
        } catch (e: ApiException) {
            // error msg
            Toast.makeText(this, "Sign-in failed: ${e.statusCode}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun goToMainActivity(account: GoogleSignInAccount) {
        // flow to main, cant go back to login, needs to logout, ps. added in logout
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
