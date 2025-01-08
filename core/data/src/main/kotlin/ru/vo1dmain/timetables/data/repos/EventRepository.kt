package ru.vo1dmain.timetables.data.repos

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.models.Event
import ru.vo1dmain.timetables.data.sources.event.EventDataSource

class EventRepository(private val source: EventDataSource) {
    fun findAllForDate(date: Instant): Flow<List<Event>> {
        return source.findAllForDate(date)
    }
    
    suspend fun create(event: Event): Int {
        return source.create(event)
    }
    
    suspend fun upsert(event: Event): Int {
        return source.upsert(event)
    }
    
    suspend fun findById(id: Int): Event? {
        return source.findById(id)
    }
}