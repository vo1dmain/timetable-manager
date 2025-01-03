package ru.vo1dmain.timetables.instructors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.instructors.edit.InstructorEdit
import ru.vo1dmain.timetables.ui.Previews
import ru.vo1dmain.timetables.ui.TopBarScaffoldScreen
import ru.vo1dmain.timetables.ui.R as UiR

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
        onEditClick = {
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
    onEditClick: () -> Unit = {},
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
        Image(
            painter = rememberAsyncImagePainter(
                model = state.image,
                fallback = painterResource(R.drawable.rounded_person_24_filled)
            ),
            contentDescription = "Image",
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clip(shapes.extraLarge)
                .requiredSize(largeImageSize)
                .background(colorScheme.secondaryContainer)
        )
        
        Spacer(modifier = Modifier.height(smallSpacerSize * 2))
        
        Text(
            text = state.name,
            style = typography.headlineSmall.copy(fontWeight = FontWeight.W700),
            maxLines = 2
        )
        
        Spacer(modifier = Modifier.height(smallSpacerSize))
        
        if (state.title != null) {
            Text(
                text = state.title,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(smallSpacerSize))
        }
        
        OutlinedButton(
            onClick = onEditClick,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(),
            shape = shapes.medium
        ) {
            Text(text = stringResource(UiR.string.action_edit))
        }
        
        Spacer(modifier = Modifier.height(mediumSpacerSize))
        
        if (state.email != null) {
            HorizontalDivider(modifier = Modifier.height(2.dp))
            
            Column(
                modifier = Modifier
                    .padding(vertical = smallSpacerSize)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.label_contacts),
                    style = typography.labelLarge
                )
                
                Spacer(modifier = Modifier.height(smallSpacerSize))
                
                Surface(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = mediumSpacerSize, horizontal = smallSpacerSize),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_mail_24),
                            contentDescription = stringResource(R.string.label_email)
                        )
                        
                        Spacer(modifier = Modifier.width(smallSpacerSize))
                        
                        Text(text = state.email)
                    }
                }
            }
        }
    }
}

@Previews
@Composable
private fun Preview() {
    AppTheme {
        InstructorLayout(
            state = InstructorState(
                name = "Ivanov Ivan Ivanovich",
                title = "Programming",
                email = "sample@mail.com"
            ),
            snackbarHostState = SnackbarHostState()
        )
    }
}