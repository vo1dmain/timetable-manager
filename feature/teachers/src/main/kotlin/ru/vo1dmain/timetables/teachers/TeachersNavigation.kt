package ru.vo1dmain.timetables.teachers

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.teachers.edit.TeacherEditScreen
import ru.vo1dmain.timetables.teachers.list.TeachersListScreen

@Serializable
internal data class TeacherEdit(val id: Int? = null)

@Serializable
internal data class TeacherScreen(val id: Int)

@Serializable
internal object TeachersList

@Serializable
object TeachersNavigation

fun NavGraphBuilder.teachersNavigation(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit,
    onNavigateUp: () -> Unit
) {
    navigation<TeachersNavigation>(startDestination = TeachersList) {
        composable<TeachersList> {
            TeachersListScreen(snackbarHostState, onNavigateTo)
        }
        
        composable<TeacherScreen> {
            TeacherScreen(snackbarHostState, onNavigateTo, onNavigateUp)
        }
        
        composable<TeacherEdit> {
            TeacherEditScreen(snackbarHostState, onNavigateUp)
        }
    }
}