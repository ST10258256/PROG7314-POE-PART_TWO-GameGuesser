package com.example.gameguesser.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SettingsViewModel : ViewModel() {
    private val _darkModeEnabled = MutableLiveData(false)
    val darkModeEnabled: LiveData<Boolean> = _darkModeEnabled

    fun setDarkMode(enabled: Boolean) {
        _darkModeEnabled.value = enabled
        // persist to DataStore/SharedPreferences as needed
    }
}
