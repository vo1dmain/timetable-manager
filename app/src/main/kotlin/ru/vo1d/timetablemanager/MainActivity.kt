package ru.vo1d.timetablemanager

import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.vo1d.timetablemanager.ui.sections.settings.SettingsFragment

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPreferences()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navController = findNavController(R.id.nav_host_fragment)

        navigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, _, _ ->
            actionMode?.finish()
            hideKeyboard()
        }
    }

    private fun setThemeFromPreferences() {
        val themeId = PreferenceManager
            .getDefaultSharedPreferences(applicationContext)
            .getInt(
                SettingsFragment.themeSharedKey,
                R.style.Theme_TimetableManager_Cyan
            )
        setTheme(themeId)
    }

    private fun hideKeyboard() {
        getSystemService(InputMethodManager::class.java)?.let {
            val token = window.decorView.windowToken
            it.hideSoftInputFromWindow(token, 0)
        }
    }


    companion object {
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_timetable,
            R.id.nav_subjects,
            R.id.nav_instructors,
            R.id.nav_preferences
        ).build()

        var actionMode: ActionMode? = null
    }
}