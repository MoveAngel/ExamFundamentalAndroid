package com.exam.fundanavapii.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ThemeViewModel(application: Application) : AndroidViewModel(application) {

    private val sharedPreferences = application.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    private val _isDarkMode = MutableLiveData(loadThemePreference())
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    fun setDarkMode(isDarkMode: Boolean) {
        if (_isDarkMode.value != isDarkMode) {
            _isDarkMode.value = isDarkMode
            saveThemePreference(isDarkMode)
        }
    }

    private fun saveThemePreference(isDarkMode: Boolean) {
        sharedPreferences.edit().putBoolean("DARK_MODE", isDarkMode).apply()
    }

    private fun loadThemePreference(): Boolean {
        return sharedPreferences.getBoolean("DARK_MODE", false)
    }
}
