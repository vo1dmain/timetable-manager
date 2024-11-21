package ru.vo1dmain.timetables

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.runBlocking
import ru.vo1dmain.timetables.prefs.extensions.preferences
import ru.vo1dmain.timetables.prefs.extensions.theme

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        runBlocking {
            val nightMode = preferences.theme.last()
            AppCompatDelegate.setDefaultNightMode(nightMode)
        }
    }
}