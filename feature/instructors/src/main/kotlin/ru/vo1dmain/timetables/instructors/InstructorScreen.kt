package ru.vo1dmain.timetables.instructors

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.instructors.edit.InstructorEdit

@Serializable
internal data class Instructor(val id: Int)

@Composable
internal fun InstructorScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToEdit: (InstructorEdit) -> Unit,
    onNavigateUp: () -> Unit
) {

}