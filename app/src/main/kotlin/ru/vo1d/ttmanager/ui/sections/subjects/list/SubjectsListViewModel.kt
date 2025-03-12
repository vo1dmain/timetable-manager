package ru.vo1d.ttmanager.ui.sections.subjects.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vo1d.ttmanager.data.entities.subjects.SubjectsRepository
import ru.vo1d.ttmanager.ui.common.selection.SelectableListViewModel

internal class SubjectsListViewModel(application: Application) :
    SelectableListViewModel<Long>(application) {
    private val repo = SubjectsRepository(application)
    
    val all by lazy { repo.all }
    
    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }
}