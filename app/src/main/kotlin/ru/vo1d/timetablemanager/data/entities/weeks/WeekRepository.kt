package ru.vo1d.timetablemanager.data.entities.weeks

import android.app.Application
import ru.vo1d.timetablemanager.data.BaseRepository
import ru.vo1d.timetablemanager.data.TimetableDb

internal class WeekRepository(application: Application) : BaseRepository<Week, WeeksDao>() {
    override val dao = TimetableDb.instance(application).weeksDao()
}