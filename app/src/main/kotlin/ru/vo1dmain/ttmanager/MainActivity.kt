package ru.vo1dmain.ttmanager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.vo1dmain.ttmanager.ui.sections.settings.SettingsFragment
import ru.vo1dmain.ttmanager.design.R as DesignR

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
                SettingsFragment.THEME_SHARED_KEY,
                DesignR.style.Theme_TimetableManager_Cyan
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