package ru.vo1dmain.timetables.subjects

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
internal data class SubjectScreen(val id: Int)

@Composable
internal fun SubjectScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit,
    onNavigateUp: () -> Unit
) {

}