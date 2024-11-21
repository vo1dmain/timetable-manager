package ru.vo1dmain.timetables.data.entities.timetable

import android.app.Application
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DayOfWeek
import ru.vo1dmain.timetables.data.TimetableDb

class TimetablesRepository(application: Application) {
    private val db = TimetableDb.instance(application)
    private val sessionsDao = db.sessionsDao()
    private val weeksDao = db.weeksDao()
    
    val allWeeks get() = weeksDao.all
    
    fun findAllDaysForWeek(weekId: Int): Flow<List<DayOfWeek>> {
        return sessionsDao.findAllDaysForWeek(weekId)
    }
}