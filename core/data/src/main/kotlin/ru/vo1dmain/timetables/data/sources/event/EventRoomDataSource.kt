package ru.vo1dmain.timetables.data.sources.event

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.datetime.Instant
import ru.vo1dmain.timetables.data.TimetableDb
import ru.vo1dmain.timetables.data.entities.event.EventDao
import ru.vo1dmain.timetables.data.entities.event.EventEntity
import ru.vo1dmain.timetables.data.models.Event
import ru.vo1dmain.timetables.data.toEntity
import ru.vo1dmain.timetables.data.toModel

class EventRoomDataSource internal constructor(private val dao: EventDao) : EventDataSource {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun findAllForDate(date: Instant): Flow<List<Event>> {
        return dao.findAllForDate(date).mapLatest { it.map(EventEntity::toModel) }
    }
    
    override suspend fun create(item: Event): Int {
        return dao.insert(item.toEntity()).toInt()
    }
    
    override suspend fun findById(id: Int): Event? {
        return dao.findById(id)?.toModel()
    }
    
    override suspend fun update(item: Event) {
        dao.update(item.toEntity())
    }
    
    override suspend fun upsert(item: Event): Int {
        return dao.upsert(item.toEntity()).toInt()
    }
    
    override suspend fun delete(item: Event) {
        dao.delete(item.toEntity())
    }
    
    companion object {
        fun instance(application: Application): EventDataSource {
            return EventRoomDataSource(TimetableDb.instance(application).eventDao())
        }
    }
}