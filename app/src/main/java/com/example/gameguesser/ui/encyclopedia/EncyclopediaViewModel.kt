package com.example.gameguesser.ui.encyclopedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EncyclopediaViewModel : ViewModel() {
    // expose entries as List<String>
    private val _entries = MutableLiveData<List<String>>(listOf(
        "Example Game — Short description here.",
        "Another Game — Another description."
    ))
    val entries: LiveData<List<String>> = _entries
}
