package ru.vo1dmain.timetables.teachers

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.entities.teacher.TeachersRepository

internal class TeacherViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = TeachersRepository(application)
    
    init {
        viewModelScope.launch {
            val record = repo.findById(id)
            state = TeacherState(
                image = record.image,
                name = record.name,
                title = record.title,
                email = record.email
            )
        }
    }
    
    val id = savedStateHandle.toRoute<TeacherScreen>().id
    
    var state by mutableStateOf(TeacherState())
        private set
    
}

@Immutable
internal data class TeacherState(
    val image: String? = null,
    val name: String = "",
    val title: String? = null,
    val email: String? = null
)