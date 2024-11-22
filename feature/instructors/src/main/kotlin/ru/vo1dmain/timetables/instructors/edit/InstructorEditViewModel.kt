package ru.vo1dmain.timetables.instructors.edit

import android.app.Application
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.entities.instructor.Instructor
import ru.vo1dmain.timetables.data.entities.instructor.InstructorsRepository

internal class InstructorEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = InstructorsRepository(application)
    
    private val id = savedStateHandle.toRoute<InstructorEdit>().id
    
    init {
        if (id != null) {
            viewModelScope.launch {
                val record = repo.findById(id)
                name.value = record.fullName
                email.value = record.email
            }
        }
    }
    
    val image = mutableStateOf<String?>(null)
    val name = mutableStateOf("")
    val email = mutableStateOf<String?>(null)
    
    val isEditMode get() = id != null
    
    val canBeSubmitted = derivedStateOf { name.value.isNotBlank() }
    
    fun submit() {
        viewModelScope.launch {
            val nameParts = name.value.split(' ')
            
            val instructor = Instructor(
                id = id ?: DatabaseEntity.DEFAULT_ID,
                firstName = nameParts.elementAtOrElse(0) { name.value },
                middleName = nameParts.elementAtOrNull(1),
                lastName = nameParts.elementAtOrNull(2),
                email = email.value?.trim(),
                image = image.value
            )
            
            tryUpsert(instructor)
        }
    }
    
    private suspend fun tryUpsert(instructor: Instructor) {
        try {
            repo.upsert(instructor)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}