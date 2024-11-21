package ru.vo1dmain.timetables.instructors

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle

internal class InstructorViewModel(
    savedStateHandle: SavedStateHandle,
    application: Application
) : AndroidViewModel(application)