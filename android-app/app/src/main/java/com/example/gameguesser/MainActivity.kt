package com.example.gameguesser

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.gameguesser.Database.AppDatabase
import com.example.gameguesser.data.RetrofitClient
import com.example.gameguesser.databinding.ActivityMainBinding
import com.example.gameguesser.repository.GameRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Start background sync (optional)
        val dao = AppDatabase.getDatabase(this).gameDao()
        val repository = GameRepository(dao, RetrofitClient.api)
        CoroutineScope(Dispatchers.IO).launch {
            repository.syncFromApi()
        }

        // ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        // Nav setup
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_encyclopedia,
                R.id.navigation_chatbot,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // --- WindowInsets: make the BottomNavigationView sit above the system gesture/navigation area ---
        ViewCompat.setOnApplyWindowInsetsListener(navView) { v, insets ->
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            // Keep existing left/top/right padding, only set bottom padding to navBarInset
            v.updatePadding(bottom = navBarInset)
            insets
        }

        // Also add bottom inset to the nav host container so fragment content isn't hidden by nav view
        val navHost: View = findViewById(R.id.nav_host_fragment_activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(navHost) { v, insets ->
            val navBarInset = insets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom
            v.updatePadding(bottom = navBarInset)
            insets
        }
    }
}
