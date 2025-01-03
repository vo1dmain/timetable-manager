package ru.vo1dmain.timetables.teachers.list

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.teachers.TeacherScreen

@Serializable
internal object TeachersList

@Composable
internal fun TeachersListScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToTeacher: (TeacherScreen) -> Unit
) {

}