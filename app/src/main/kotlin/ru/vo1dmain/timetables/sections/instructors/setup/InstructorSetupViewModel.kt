package ru.vo1dmain.timetables.sections.instructors.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.vo1dmain.timetables.data.entities.instructor.Instructor
import ru.vo1dmain.timetables.data.entities.instructor.InstructorsRepository
import ru.vo1dmain.timetables.ui.Submitter

@OptIn(ExperimentalCoroutinesApi::class)
internal class InstructorSetupViewModel(application: Application) :
    AndroidViewModel(application), Submitter {
    
    private val repo = InstructorsRepository(application)
    
    private val firstName = MutableStateFlow("")
    private val middleName = MutableStateFlow("")
    private val lastName = MutableStateFlow("")
    private val email = MutableStateFlow("")
    
    private val firstNameIsSet by lazy { firstName.mapLatest(String::isNotBlank) }
    private val middleNameIsSet by lazy { middleName.mapLatest(String::isNotBlank) }
    private val lastNameIsSet by lazy { lastName.mapLatest(String::isNotBlank) }
    
    val canBeSubmitted by lazy {
        combine(firstNameIsSet, middleNameIsSet, lastNameIsSet) { states ->
            states.all { it }
        }
    }
    
    
    override fun submit(onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = trySubmit()
            withContext(Dispatchers.Main) {
                onResult(result)
            }
        }
    }
    
    fun setFirstName(value: String) =
        firstName.update { value }
    
    fun setMiddleName(value: String) =
        middleName.update { value }
    
    fun setLastName(value: String) =
        lastName.update { value }
    
    fun setEmail(value: String) =
        email.update { value }
    
    private suspend fun trySubmit() = try {
        val item = Instructor(
            firstName = firstName.value.trim(),
            middleName = middleName.value.trim(),
            lastName = lastName.value.trim(),
            email = email.value.trim()
        )
        repo.insert(item) != -1L
    } catch (e: Exception) {
        false
    }
}