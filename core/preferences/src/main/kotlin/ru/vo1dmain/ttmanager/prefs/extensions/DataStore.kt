package ru.vo1dmain.ttmanager.prefs.extensions

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.map
import ru.vo1dmain.ttmanager.prefs.dynamicColorSharedKey
import ru.vo1dmain.ttmanager.prefs.themeSharedKey

val themeKey = intPreferencesKey(themeSharedKey)

val dynamicColorKey = booleanPreferencesKey(dynamicColorSharedKey)

val DataStore<Preferences>.theme
    get() = data.map {
        it[themeKey] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

val DataStore<Preferences>.dynamicColor
    get() = data.map {
        it[dynamicColorKey] ?: false
    }