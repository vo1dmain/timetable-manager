package ru.vo1dmain.timetables.instructors.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.instructors.Instructor

@Serializable
internal object InstructorsList

@Composable
internal fun InstructorsListScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToInstructor: (Instructor) -> Unit
) {

}