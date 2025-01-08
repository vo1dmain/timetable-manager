package ru.vo1dmain.timetables.teachers

import android.app.Application
import androidx.compose.runtime.Immutable
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.repos.TeachersRepository
import ru.vo1dmain.timetables.data.sources.teacher.TeacherRoomDataSource

internal class TeacherViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = TeachersRepository(TeacherRoomDataSource(application))
    
    private val _uiState = MutableStateFlow(TeacherScreenState())
    
    val id = savedStateHandle.toRoute<TeacherScreen>().id
    
    val uiState = _uiState.asStateFlow()
    
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val record = repo.findById(id) ?: return@launch
            _uiState.update {
                TeacherScreenState(
                    image = record.image,
                    name = record.name,
                    title = record.title,
                    email = record.email
                )
            }
        }
    }
}

@Immutable
internal data class TeacherScreenState(
    val image: String? = null,
    val name: String = "",
    val title: String? = null,
    val email: String? = null
)