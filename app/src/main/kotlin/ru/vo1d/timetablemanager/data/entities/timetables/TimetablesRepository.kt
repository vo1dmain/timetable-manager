package ru.vo1d.timetablemanager.data.entities.timetables

import android.app.Application
import ru.vo1d.timetablemanager.data.TimetableDb

internal class TimetablesRepository(application: Application) {
    private val db = TimetableDb.instance(application)
    private val sessionsDao = db.sessionsDao()
    private val weeksDao = db.weeksDao()

    val allWeeks by lazy { weeksDao.all }


    fun findAllDaysForWeek(weekId: Int) =
        sessionsDao.findAllDaysForWeek(weekId)
}