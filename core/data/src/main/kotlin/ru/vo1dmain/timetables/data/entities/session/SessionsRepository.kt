package ru.vo1dmain.timetables.data.entities.session

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb

class SessionsRepository(application: Application) :
    BaseRepository<Session, SessionsDao>(TimetableDb.instance(application).sessionsDao()) {
    
    fun findAllForDay(day: DayOfWeek): Flow<List<Session>> {
        return dao.findAllForDay(day)
    }
    
    suspend fun findById(id: Int): Session? {
        return dao.findById(id)
    }
}