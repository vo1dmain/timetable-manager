package ru.vo1dmain.timetables.instructors.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
internal data class InstructorEdit(val id: Int)

@Composable
internal fun InstructorEditScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {

}