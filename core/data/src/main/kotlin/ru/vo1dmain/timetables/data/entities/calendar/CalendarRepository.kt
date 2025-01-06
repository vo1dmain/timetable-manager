package ru.vo1dmain.timetables.data.entities.calendar

import android.app.Application
import ru.vo1dmain.timetables.data.TimetableDb

class CalendarRepository(application: Application) {
    private val db = TimetableDb.instance(application)
    private val sessionsDao = db.eventsDao()
}