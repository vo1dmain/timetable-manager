package ru.vo1dmain.timetables.calendar.events.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import ru.vo1dmain.timetables.data.repos.EventRepository
import ru.vo1dmain.timetables.data.sources.event.EventRoomDataSource

internal class EventsListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = EventRepository(EventRoomDataSource(application))
}