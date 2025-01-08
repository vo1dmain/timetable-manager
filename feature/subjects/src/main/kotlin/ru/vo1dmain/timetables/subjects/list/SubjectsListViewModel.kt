package ru.vo1dmain.timetables.subjects.list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.repos.SubjectRepository
import ru.vo1dmain.timetables.data.sources.subject.SubjectRoomDataSource

internal class SubjectsListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = SubjectRepository(SubjectRoomDataSource(application))
    
    private val selected = mutableSetOf<Int>()
    
    val all by lazy {
        repo.getAll().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )
    }
    
    fun deleteSelected() {
        viewModelScope.launch {
            repo.deleteByIds(selected)
        }
    }
}