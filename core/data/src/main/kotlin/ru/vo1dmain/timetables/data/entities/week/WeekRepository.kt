package ru.vo1dmain.timetables.data.entities.week

import android.app.Application
import ru.vo1dmain.timetables.data.BaseRepository
import ru.vo1dmain.timetables.data.TimetableDb

internal class WeekRepository(application: Application) : BaseRepository<Week, WeeksDao>() {
    override val dao = TimetableDb.instance(application).weeksDao()
}