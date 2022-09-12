package ru.vo1d.timetablemanager.ui.sections.instructors.setup

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine

internal class InstructorSetupViewModel(application: Application) : AndroidViewModel(application) {
    private val firstNameIsSet = MutableStateFlow(false)
    private val middleNameIsSet = MutableStateFlow(false)
    private val lastNameIsSet = MutableStateFlow(false)

    val canBeSaved = combine(firstNameIsSet, middleNameIsSet, lastNameIsSet) { states ->
        states.all { it }
    }
}