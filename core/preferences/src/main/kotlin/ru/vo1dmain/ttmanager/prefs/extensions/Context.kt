package ru.vo1dmain.ttmanager.prefs.extensions

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.preferences by preferencesDataStore(name = "settings")