package ru.vo1dmain.ttmanager.ui.sections.timetable

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import ru.vo1dmain.ttmanager.data.DatabaseEntity.Companion.INVALID_ID
import ru.vo1dmain.ttmanager.data.entities.timetables.TimetablesRepository

@OptIn(ExperimentalCoroutinesApi::class)
internal class TimetableViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = TimetablesRepository(application)
    
    private val _selectedWeek = MutableStateFlow(INVALID_ID)
    
    val selectedWeek by lazy { _selectedWeek.asStateFlow() }
    val allWeeks by lazy { repo.allWeeks }
    val daysForCurrentWeek by lazy { _selectedWeek.flatMapLatest { repo.findAllDaysForWeek(it) } }
    
    
    fun selectWeek(weekId: Int) {
        _selectedWeek.update { weekId }
    }
}