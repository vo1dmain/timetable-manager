package ru.vo1dmain.timetables.settings.appearance

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.prefs.extensions.dynamicColorKey
import ru.vo1dmain.timetables.prefs.extensions.preferences
import ru.vo1dmain.timetables.prefs.extensions.themeKey

internal class AppearanceViewModel(application: Application) : AndroidViewModel(application) {
    private val preferences = application.preferences
    
    private val themeStateKey = stringPreferencesKey("theme_state")
    
    val theme = preferences.data
        .map { it[themeStateKey] ?: "System" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "System"
        )
    
    val dynamicColor = preferences.data
        .map { it[dynamicColorKey] ?: false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )
    
    fun selectTheme(value: String) {
        saveTheme(value)
    }
    
    fun toggleDynamicColor(value: Boolean) {
        saveDynamicColor(value)
    }
    
    private fun saveTheme(value: String) {
        viewModelScope.launch {
            val theme = when (value) {
                "System" -> MODE_NIGHT_FOLLOW_SYSTEM
                "Day" -> MODE_NIGHT_NO
                "Night" -> MODE_NIGHT_YES
                else -> return@launch
            }
            
            preferences.edit {
                it[themeKey] = theme
                it[themeStateKey] = value
            }
        }
    }
    
    private fun saveDynamicColor(value: Boolean) {
        viewModelScope.launch {
            preferences.edit {
                it[dynamicColorKey] = value
            }
        }
    }
}