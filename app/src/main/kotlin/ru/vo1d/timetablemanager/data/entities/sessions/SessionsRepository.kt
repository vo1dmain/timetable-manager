package ru.vo1d.timetablemanager.data.entities.sessions

import android.app.Application
import kotlinx.datetime.DayOfWeek
import ru.vo1d.timetablemanager.data.BaseRepository
import ru.vo1d.timetablemanager.data.TimetableDb

class SessionsRepository(application: Application) : BaseRepository<Int, Session, SessionsDao>() {
    override val dao = TimetableDb.instance(application).classDao()

    fun findAllForDay(day: DayOfWeek) =
        dao.findAllForDay(day)


    suspend fun findById(id: Int) =
        dao.findById(id)
}