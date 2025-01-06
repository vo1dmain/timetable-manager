package ru.vo1dmain.timetables.calendar

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
object Calendar

@Composable
internal fun CalendarScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit
) {

}