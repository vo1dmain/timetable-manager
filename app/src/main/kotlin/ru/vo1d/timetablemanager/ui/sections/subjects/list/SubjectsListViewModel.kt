package ru.vo1d.timetablemanager.ui.sections.subjects.list

import android.app.Application
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.vo1d.timetablemanager.data.entities.subjects.Subject
import ru.vo1d.timetablemanager.data.entities.subjects.SubjectsDao
import ru.vo1d.timetablemanager.data.entities.subjects.SubjectsRepository
import ru.vo1d.timetablemanager.ui.utils.SelectableListViewModel

internal class SubjectsListViewModel(application: Application) :
    SelectableListViewModel<Subject, SubjectsDao>(application) {
    override val repo = SubjectsRepository(application)

    val all by lazy { repo.all }

    fun deleteAll() {
        viewModelScope.launch {
            repo.deleteAll()
        }
    }
}