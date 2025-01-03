package ru.vo1dmain.timetables.instructors

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
import ru.vo1dmain.timetables.data.entities.instructor.InstructorsRepository

internal class InstructorViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = InstructorsRepository(application)
    
    init {
        viewModelScope.launch {
            val record = repo.findById(id)
            state = InstructorState(
                image = record.image,
                name = record.fullName,
                title = record.title,
                email = record.email
            )
        }
    }
    
    val id = savedStateHandle.toRoute<InstructorScreen>().id
    
    var state by mutableStateOf(InstructorState())
        private set
    
}

@Immutable
internal data class InstructorState(
    val image: String? = null,
    val name: String = "",
    val title: String? = null,
    val email: String? = null
)