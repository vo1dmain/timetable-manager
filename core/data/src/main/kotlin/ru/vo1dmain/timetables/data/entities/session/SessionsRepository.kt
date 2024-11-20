package ru.vo1dmain.timetables.data.entities.session

import android.app.Application
import kotlinx.datetime.DayOfWeek
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb

class SessionsRepository(application: Application) : BaseRepository<Session, SessionsDao>() {
    override val dao = TimetableDb.instance(application).sessionsDao()
    
    fun findAllForDay(day: DayOfWeek) =
        dao.findAllForDay(day)
    
    
    suspend fun findById(id: Int) =
        dao.findById(id)
}