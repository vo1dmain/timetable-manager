package ru.vo1dmain.timetables.data.entities.timetable

import android.app.Application
import ru.vo1dmain.timetables.data.TimetableDb

class TimetablesRepository(application: Application) {
    private val db = TimetableDb.instance(application)
    private val sessionsDao = db.sessionsDao()
    private val weeksDao = db.weeksDao()
    
    val allWeeks by lazy { weeksDao.all }
    
    
    fun findAllDaysForWeek(weekId: Int) =
        sessionsDao.findAllDaysForWeek(weekId)
}