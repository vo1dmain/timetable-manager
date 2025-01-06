package ru.vo1dmain.timetables.subjects.edit

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.entities.session.SessionType
import ru.vo1dmain.timetables.data.entities.subject.Subject
import ru.vo1dmain.timetables.data.entities.subject.SubjectTeacher
import ru.vo1dmain.timetables.data.entities.subject.SubjectsRepository
import ru.vo1dmain.timetables.data.entities.teacher.Teacher

internal class SubjectEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = SubjectsRepository(application)
    
    private val id = savedStateHandle.toRoute<SubjectEdit>().id
    private val teachersState = mutableStateOf<List<Teacher>>(emptyList())
    private val typesState = mutableStateOf<List<SessionType>>(emptyList())
    
    val state = EditScreenState()
    
    val isEditMode get() = id != null
    
    init {
        if (id != null) {
            viewModelScope.launch {
                val record = repo.findById(id) ?: return@launch
                
                state.title.setTextAndPlaceCursorAtEnd(record.title)
                typesState.value = record.types
            }
        }
    }
    
    fun submit() {
        viewModelScope.launch {
            val subject = Subject(
                id = id ?: DatabaseEntity.DEFAULT_ID,
                title = state.title.text.toString().trim(),
                types = state.selectedTypes
            )
            
            trySubmit(subject)
        }
    }
    
    fun selectType(type: SessionType) {
        typesState.value += type
    }
    
    fun unselectType(type: SessionType) {
        typesState.value -= type
    }
    
    fun selectTeacher(teacher: Teacher) {
        teachersState.value += teacher
    }
    
    fun unselectTeacher(teacher: Teacher) {
        teachersState.value -= teacher
    }
    
    private suspend fun trySubmit(subject: Subject) {
        try {
            val id = repo.insert(subject).toInt()
            
            if (id == -1) return
            
            state.selectedTeachers.forEach {
                val pair = SubjectTeacher(id, it.id)
                repo.insertPair(pair)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

@Stable
internal class EditScreenState(
    val title: TextFieldState = TextFieldState(),
    teachersState: MutableState<List<Teacher>> = mutableStateOf(emptyList()),
    typesState: MutableState<List<SessionType>> = mutableStateOf(emptyList())
) {
    val selectedTeachers by teachersState
    val selectedTypes by typesState
    
    val canBeSubmitted by derivedStateOf {
        title.text.isNotBlank() && selectedTeachers.isNotEmpty() && selectedTypes.isNotEmpty()
    }
}