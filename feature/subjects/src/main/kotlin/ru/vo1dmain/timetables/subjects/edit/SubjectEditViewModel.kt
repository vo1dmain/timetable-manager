package ru.vo1dmain.timetables.subjects.edit

import android.app.Application
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.DEFAULT_ID
import ru.vo1dmain.timetables.data.models.EventType
import ru.vo1dmain.timetables.data.models.Subject
import ru.vo1dmain.timetables.data.models.Teacher
import ru.vo1dmain.timetables.data.repos.SubjectRepository
import ru.vo1dmain.timetables.data.sources.subject.SubjectRoomDataSource
import ru.vo1dmain.timetables.subjects.SubjectEdit

internal class SubjectEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = SubjectRepository(SubjectRoomDataSource(application))
    
    private val teachers = mutableStateOf<List<Teacher>>(emptyList())
    private val types = mutableStateOf<List<EventType>>(emptyList())
    
    val id = savedStateHandle.toRoute<SubjectEdit>().id
    
    val state = EditScreenState(
        teachers = teachers,
        types = types
    )
    
    val isCreationMode get() = id == null
    
    init {
        if (id != null) {
            viewModelScope.launch(Dispatchers.IO) {
                val model = repo.findById(id) ?: return@launch
                
                state.title.setTextAndPlaceCursorAtEnd(model.title)
                types.value = model.types
                teachers.value = model.teachers
            }
        }
    }
    
    fun submit() {
        viewModelScope.launch(Dispatchers.IO) {
            val subject = Subject(
                id = id ?: DEFAULT_ID,
                title = state.title.text.toString().trim(),
                types = state.selectedTypes,
                teachers = teachers.value
            )
            
            repo.upsert(subject)
        }
    }
    
    fun toggleType(type: EventType, selected: Boolean) {
        if (selected) types.value += type
        else types.value -= type
    }
    
    fun selectTeacher(teacher: Teacher) {
        teachers.value += teacher
    }
    
    fun unselectTeacher(teacher: Teacher) {
        teachers.value -= teacher
    }
}

@Stable
internal class EditScreenState(
    val title: TextFieldState = TextFieldState(),
    teachers: State<List<Teacher>> = mutableStateOf(emptyList()),
    types: State<List<EventType>> = mutableStateOf(emptyList())
) {
    val selectedTeachers by teachers
    val selectedTypes by types
    
    val canBeSubmitted by derivedStateOf {
        title.text.isNotBlank() && selectedTeachers.isNotEmpty() && selectedTypes.isNotEmpty()
    }
}