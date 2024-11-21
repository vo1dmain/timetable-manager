package ru.vo1dmain.timetables.instructors

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.instructors.edit.InstructorEdit
import ru.vo1dmain.timetables.instructors.edit.InstructorEditScreen
import ru.vo1dmain.timetables.instructors.list.InstructorsList
import ru.vo1dmain.timetables.instructors.list.InstructorsListScreen

@Serializable
object InstructorsNavigation

fun NavGraphBuilder.instructorsNavigation(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit,
    onNavigateUp: () -> Unit
) {
    navigation<InstructorsNavigation>(startDestination = InstructorsList) {
        composable<InstructorsList> {
            InstructorsListScreen(snackbarHostState, onNavigateTo)
        }
        
        composable<Instructor> {
            InstructorScreen(snackbarHostState, onNavigateTo, onNavigateUp)
        }
        
        composable<InstructorEdit> {
            InstructorEditScreen(snackbarHostState, onNavigateUp)
        }
    }
}