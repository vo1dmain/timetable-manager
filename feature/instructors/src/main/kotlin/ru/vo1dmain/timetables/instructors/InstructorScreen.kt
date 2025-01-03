package ru.vo1dmain.timetables.instructors

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.instructors.edit.InstructorEdit
import ru.vo1dmain.timetables.ui.Previews
import ru.vo1dmain.timetables.ui.TopBarScaffoldScreen

@Serializable
internal data class InstructorScreen(val id: Int)

@Composable
internal fun InstructorScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateToEdit: (InstructorEdit) -> Unit,
    onNavigateUp: () -> Unit
) {
    val viewModel = viewModel<InstructorViewModel>()
    
    InstructorLayout(
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onNavigateToEdit = {
            onNavigateToEdit(InstructorEdit(viewModel.id))
        },
        onNavigateUp = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstructorLayout(
    state: InstructorState,
    snackbarHostState: SnackbarHostState,
    onNavigateToEdit: (InstructorEdit) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()
    
    TopBarScaffoldScreen(
        title = stringResource(R.string.screen_title_instructor),
        snackbarHostState = snackbarHostState,
        contentModifier = Modifier.padding(contentPadding),
        topAppBarState = topAppBarState,
        scrollBehavior = scrollBehavior,
        scrollState = scrollState,
        onNavigateUp = onNavigateUp,
    ) {
    
    }
}

@Previews
@Composable
private fun Preview() {
    AppTheme {
        InstructorLayout(
            state = InstructorState(),
            snackbarHostState = SnackbarHostState()
        )
    }
}