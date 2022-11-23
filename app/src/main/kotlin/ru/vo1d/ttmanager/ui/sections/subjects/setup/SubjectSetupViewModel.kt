package ru.vo1d.ttmanager.ui.sections.subjects.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import ru.vo1d.ttmanager.data.entities.instructors.Instructor
import ru.vo1d.ttmanager.data.entities.sessions.SessionType

internal class SubjectSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val _selectedTypes = MutableStateFlow(mutableListOf<SessionType>())
    val selectedTypes = _selectedTypes.asStateFlow()

    private val _selectedInstructors = MutableStateFlow(mutableListOf<Instructor>())
    val selectedInstructors = _selectedInstructors.asStateFlow()

    private val titleIsSet = MutableStateFlow(false)
    private val instructorIsSet = MutableStateFlow(false)
    private val typeIsSelected = MutableStateFlow(false)


    val canBeSaved = combine(titleIsSet, instructorIsSet, typeIsSelected) { states ->
        states.all { it }
    }


    fun selectType(type: SessionType) {
        _selectedTypes.update { it.apply { add(type) } }
    }

    fun unselectType(type: SessionType) {
        _selectedTypes.update { it.apply { remove(type) } }
    }

    fun selectInstructor(instructor: Instructor) {
        _selectedInstructors.update { it.apply { add(instructor) } }
    }

    fun unselectInstructor(instructor: Instructor) {
        _selectedInstructors.update { it.apply { remove(instructor) } }
    }
}