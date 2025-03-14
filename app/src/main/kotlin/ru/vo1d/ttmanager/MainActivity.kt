package ru.vo1d.ttmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.vo1d.ttmanager.ui.sections.settings.SettingsFragment

internal class MainActivity : AppCompatActivity(), ActionModeOwner {
    override var actionMode: ActionMode? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        setThemeFromPreferences()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val navigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        navigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, _, _ ->
            actionMode?.finish()
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
    
    
    companion object {
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_timetable,
            R.id.nav_subjects,
            R.id.nav_instructors,
            R.id.nav_preferences
        ).build()
    }
}