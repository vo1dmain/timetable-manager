package ru.vo1dmain.timetables

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ActionMode
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.vo1dmain.timetables.ui.ActionModeOwner

internal class MainActivity : AppCompatActivity(), ActionModeOwner {
    override var actionMode: ActionMode? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
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
    
    companion object {
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_timetable,
            R.id.nav_subjects,
            R.id.nav_instructors,
        ).build()
    }
}