package ru.vo1d.timetablemanager.ui.sections.timetable.sessions.list

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import ru.vo1d.timetablemanager.data.entities.sessions.Session
import ru.vo1d.timetablemanager.data.entities.sessions.SessionsDao
import ru.vo1d.timetablemanager.data.entities.sessions.SessionsRepository
import ru.vo1d.timetablemanager.ui.utils.SelectableListViewModel

@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionsViewModel(application: Application) :
    SelectableListViewModel<Session, SessionsDao>(application) {
    override val repo = SessionsRepository(application)

    private val day = MutableStateFlow(DayOfWeek.MONDAY)

    val allForDay by lazy {
        day.flatMapLatest { repo.findAllForDay(it) }
    }

    fun setDay(dayOfWeek: DayOfWeek) {
        day.update { dayOfWeek }
    }
}