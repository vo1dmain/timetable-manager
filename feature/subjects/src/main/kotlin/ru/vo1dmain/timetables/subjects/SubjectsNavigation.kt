package ru.vo1dmain.timetables.subjects

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.subjects.edit.SubjectEdit
import ru.vo1dmain.timetables.subjects.edit.SubjectEditScreen
import ru.vo1dmain.timetables.subjects.list.SubjectsList
import ru.vo1dmain.timetables.subjects.list.SubjectsListScreen

@Serializable
object SubjectsNavigation

fun NavGraphBuilder.subjectsNavigation(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit,
    onNavigateUp: () -> Unit
) {
    navigation<SubjectsNavigation>(startDestination = SubjectsList) {
        composable<SubjectsList> {
            SubjectsListScreen(snackbarHostState, onNavigateTo)
        }
        
        composable<SubjectScreen> {
            SubjectScreen(snackbarHostState, onNavigateTo, onNavigateUp)
        }
        
        composable<SubjectEdit> {
            SubjectEditScreen(snackbarHostState, onNavigateUp)
        }
    }
}