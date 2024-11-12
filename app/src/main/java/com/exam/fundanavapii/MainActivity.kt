package com.exam.fundanavapii

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.exam.fundanavapii.ui.fragment.ActiveEventsFragment
import com.exam.fundanavapii.ui.fragment.CompletedEventsFragment
import com.exam.fundanavapii.ui.fragment.FavoriteEventsFragment
import com.exam.fundanavapii.ui.fragment.HomeEventsFragment
import com.exam.fundanavapii.ui.fragment.SettingsFragment
import com.exam.fundanavapii.ui.viewmodel.ThemeViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var themeViewModel: ThemeViewModel
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        themeViewModel = ViewModelProvider(this)[ThemeViewModel::class.java]
        val isDarkModeEnabled = themeViewModel.isDarkMode.value == true
        setTheme(if (isDarkModeEnabled) R.style.Theme_App_Dark else R.style.Theme_App_Light)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeEventsFragment())
                .commit()
            bottomNavigationView.selectedItemId = R.id.nav_home_events
        }

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_active_events -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ActiveEventsFragment())
                        .commit()
                    true
                }
                R.id.nav_completed_events -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, CompletedEventsFragment())
                        .commit()
                    true
                }
                R.id.nav_home_events -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeEventsFragment())
                        .commit()
                    true
                }
                R.id.nav_favorite_events -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, FavoriteEventsFragment())
                        .commit()
                    true
                }
                R.id.nav_settings -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SettingsFragment())
                        .addToBackStack(null)
                        .commit()
                    true
                }
                else -> false
            }
        }

        themeViewModel.isDarkMode.observe(this) { isDarkMode ->
            if (isDarkMode != isDarkModeEnabled) {
                recreate()
            }
        }
    }
}
