package ru.vo1dmain.timetables.subjects.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable

@Serializable
internal object SubjectsList

@Composable
internal fun SubjectsListScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit
) {
    val viewModel = viewModel<SubjectsListViewModel>()
}