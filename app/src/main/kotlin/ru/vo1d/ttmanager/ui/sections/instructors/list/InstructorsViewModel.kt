package ru.vo1d.ttmanager.ui.sections.instructors.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vo1d.ttmanager.data.entities.instructors.InstructorsRepository
import ru.vo1d.ttmanager.ui.common.selection.SelectableListViewModel

internal class InstructorsViewModel(application: Application) :
    SelectableListViewModel<Long>(application) {
    private val repo = InstructorsRepository(application)

    val all by lazy { repo.all }

    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }
}