package ru.vo1dmain.timetables.teachers.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.repos.TeachersRepository
import ru.vo1dmain.timetables.data.sources.teacher.TeacherRoomDataSource

internal class TeachersListViewModel(application: Application) :
    AndroidViewModel(application) {
    private val repo = TeachersRepository(TeacherRoomDataSource(application))
    
    private val selected = mutableSetOf<Int>()
    
    val all by lazy {
        repo.getAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = emptyList()
        )
    }
    
    fun deleteSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteByIds(selected)
        }
    }
}