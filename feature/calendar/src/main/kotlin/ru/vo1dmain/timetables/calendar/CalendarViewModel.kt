package ru.vo1dmain.timetables.calendar

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.vo1dmain.timetables.data.entities.calendar.CalendarRepository

internal class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = CalendarRepository(application)
    
}