package ru.vo1d.ttmanager.ui.sections.timetable.sessions.list

import android.app.Application
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DayOfWeek
import ru.vo1d.ttmanager.data.entities.sessions.SessionsRepository
import ru.vo1d.ttmanager.ui.common.selection.SelectableListViewModel

@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionsViewModel(application: Application) :
    SelectableListViewModel<Long>(application) {
    private val repo = SessionsRepository(application)
    
    private val day = MutableStateFlow(DayOfWeek.MONDAY)
    
    val allForDay by lazy {
        day.flatMapLatest { repo.findAllForDay(it) }
    }
    
    fun setDay(dayOfWeek: DayOfWeek) {
        day.update { dayOfWeek }
    }
}