package ru.vo1d.timetablemanager.ui.sections.instructors.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vo1d.timetablemanager.data.entities.instructors.Instructor
import ru.vo1d.timetablemanager.data.entities.instructors.InstructorsDao
import ru.vo1d.timetablemanager.data.entities.instructors.InstructorsRepository
import ru.vo1d.timetablemanager.ui.utils.SelectableListViewModel

internal class InstructorsViewModel(application: Application) :
    SelectableListViewModel<Instructor, InstructorsDao>(application) {
    override val repo = InstructorsRepository(application)

    val all by lazy { repo.all }

    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }
}