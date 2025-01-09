package ru.vo1dmain.timetables.calendar

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.calendar.events.edit.EventEditScreen

@Serializable
internal data class EventEdit(val id: Int? = null)

@Serializable
internal object Calendar

@Serializable
object CalendarNavigation

fun NavGraphBuilder.calendarNavigation(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit,
    onNavigateUp: () -> Unit
) {
    navigation<CalendarNavigation>(startDestination = Calendar) {
        composable<Calendar> {
            CalendarScreen(snackbarHostState, onNavigateTo)
        }
        
        composable<EventEdit> {
            EventEditScreen(snackbarHostState, onNavigateUp)
        }
    }
}