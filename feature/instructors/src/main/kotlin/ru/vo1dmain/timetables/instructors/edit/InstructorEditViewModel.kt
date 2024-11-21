package ru.vo1dmain.timetables.instructors.edit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vo1dmain.timetables.data.DatabaseEntity
import ru.vo1dmain.timetables.data.entities.instructor.Instructor
import ru.vo1dmain.timetables.data.entities.instructor.InstructorsRepository

@OptIn(ExperimentalCoroutinesApi::class)
internal class InstructorEditViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application) {
    private val repo = InstructorsRepository(application)
    
    private val id = savedStateHandle.toRoute<InstructorEdit>().id
    
    private val firstName = MutableStateFlow("")
    private val middleName = MutableStateFlow("")
    private val lastName = MutableStateFlow("")
    private val email = MutableStateFlow("")
    
    private val firstNameIsSet by lazy { firstName.mapLatest(String::isNotBlank) }
    private val middleNameIsSet by lazy { middleName.mapLatest(String::isNotBlank) }
    private val lastNameIsSet by lazy { lastName.mapLatest(String::isNotBlank) }
    
    init {
        if (id != null) {
            viewModelScope.launch {
                val record = repo.findById(id)
                firstName.update { record.firstName }
                middleName.update { record.middleName }
                lastName.update { record.lastName }
                email.update { record.email }
            }
        }
    }
    
    val canBeSubmitted by lazy {
        combine(firstNameIsSet, middleNameIsSet, lastNameIsSet) { states ->
            states.all { it }
        }
    }
    
    fun submit(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val instructor = Instructor(
                id = id ?: DatabaseEntity.DEFAULT_ID,
                firstName = firstName.value.trim(),
                middleName = middleName.value.trim(),
                lastName = lastName.value.trim(),
                email = email.value.trim()
            )
            
            val result = tryUpsert(instructor)
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
    
    fun setFirstName(value: String) {
        firstName.update { value }
    }
    
    fun setMiddleName(value: String) {
        middleName.update { value }
    }
    
    fun setLastName(value: String) {
        lastName.update { value }
    }
    
    fun setEmail(value: String) {
        email.update { value }
    }
    
    private suspend fun tryUpsert(instructor: Instructor): Boolean {
        return try {
            repo.upsert(instructor)
            true
        } catch (e: Exception) {
            false
        }
    }
}