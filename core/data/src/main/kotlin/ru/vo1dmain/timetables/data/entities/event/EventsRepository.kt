package ru.vo1dmain.timetables.data.entities.event

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb

class EventsRepository(application: Application) :
    BaseRepository<Event, EventsDao>(TimetableDb.instance(application).eventsDao()) {
    
    fun findAllForDate(date: Instant): Flow<List<Event>> {
        return dao.findAllForDate(date)
    }
    
    suspend fun findById(id: Int): Event? {
        return dao.findById(id)
    }
}