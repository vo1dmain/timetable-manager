package ru.vo1dmain.timetables.calendar.events.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
internal data class EventEdit(val id: Int? = null)

@Composable
internal fun EventEditScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {

}