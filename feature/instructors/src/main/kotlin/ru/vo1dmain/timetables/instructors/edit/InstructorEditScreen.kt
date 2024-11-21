package ru.vo1dmain.timetables.instructors.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
internal data class InstructorEdit(val id: Int? = null)

@Composable
internal fun InstructorEditScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {
    val viewModel = viewModel<InstructorEditViewModel>()
}