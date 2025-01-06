package ru.vo1dmain.timetables.subjects.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
internal data class SubjectEdit(val id: Int? = null)

@Composable
internal fun SubjectEditScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {

}