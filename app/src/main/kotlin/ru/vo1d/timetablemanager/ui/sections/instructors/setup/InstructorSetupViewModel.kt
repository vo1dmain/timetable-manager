package ru.vo1d.timetablemanager.ui.sections.instructors.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.vo1d.timetablemanager.data.entities.instructors.Instructor
import ru.vo1d.timetablemanager.data.entities.instructors.InstructorsRepository

@OptIn(ExperimentalCoroutinesApi::class)
internal class InstructorSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = InstructorsRepository(application)

    private val firstName = MutableStateFlow("")
    private val middleName = MutableStateFlow("")
    private val lastName = MutableStateFlow("")
    private val email = MutableStateFlow("")

    private val firstNameIsSet by lazy { firstName.mapLatest { it.isNotBlank() && it.isNotEmpty() } }
    private val middleNameIsSet by lazy { middleName.mapLatest { it.isNotBlank() && it.isNotEmpty() } }
    private val lastNameIsSet by lazy { lastName.mapLatest { it.isNotBlank() && it.isNotEmpty() } }

    val canBeSubmitted by lazy {
        combine(firstNameIsSet, middleNameIsSet, lastNameIsSet) { states ->
            states.all { it }
        }
    }


    fun submit() {
        viewModelScope.launch {
            val item = Instructor(
                firstName = firstName.value.trim(),
                middleName = middleName.value.trim(),
                lastName = lastName.value.trim(),
                email = email.value.trim()
            )
            repo.insert(item)
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
}