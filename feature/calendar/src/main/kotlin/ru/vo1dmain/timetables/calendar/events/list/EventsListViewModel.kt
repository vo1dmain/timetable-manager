package ru.vo1dmain.timetables.calendar.events.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.vo1dmain.timetables.data.entities.event.EventsRepository

internal class EventsListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = EventsRepository(application)
}