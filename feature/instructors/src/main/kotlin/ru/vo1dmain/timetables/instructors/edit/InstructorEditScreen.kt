package ru.vo1dmain.timetables.instructors.edit

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.instructors.R
import ru.vo1dmain.timetables.ui.NavigationIcon
import ru.vo1dmain.timetables.ui.Previews
import ru.vo1dmain.timetables.ui.R as UiR

@Serializable
internal data class InstructorEdit(val id: Int? = null)

@Composable
internal fun InstructorEditScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {
    val viewModel = viewModel<InstructorEditViewModel>()
    
    InstructorEditLayout(
        isEditMode = viewModel.isEditMode,
        snackbarHostState = snackbarHostState,
        image = viewModel.image,
        name = viewModel.name,
        email = viewModel.email,
        canBeSubmitted = viewModel.canBeSubmitted,
        onNavigateUp = onNavigateUp,
        onSubmit = {
            viewModel.submit()
            onNavigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InstructorEditLayout(
    isEditMode: Boolean,
    snackbarHostState: SnackbarHostState,
    image: MutableState<String?>,
    name: MutableState<String>,
    email: MutableState<String?>,
    canBeSubmitted: State<Boolean>,
    onNavigateUp: () -> Unit = {},
    onSubmit: () -> Unit = {},
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    val text = remember {
                        if (isEditMode) UiR.string.action_edit
                        else R.string.action_add_instructor
                    }
                    Text(text = stringResource(text))
                },
                navigationIcon = {
                    NavigationIcon(onNavigate = onNavigateUp)
                },
                actions = {
                    IconButton(
                        onClick = onSubmit,
                        enabled = canBeSubmitted.value
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_save_24),
                            contentDescription = stringResource(UiR.string.action_save)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    )
    { paddingValues ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            
            Image(
                painter = rememberAsyncImagePainter(
                    model = image.value,
                    fallback = painterResource(R.drawable.rounded_person_24_filled)
                ),
                contentDescription = "Image",
                modifier = Modifier
                    .clip(shapes.extraLarge)
                    .requiredSizeIn(180.dp, 180.dp)
                    .background(colorScheme.secondaryContainer)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(onClick = {}) {
                Text(text = stringResource(R.string.action_edit_photo))
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val (nameValue, onNameChanged) = name
            OutlinedTextField(
                value = nameValue,
                onValueChange = {
                    onNameChanged(it.take(40))
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.label_name))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.rounded_person_24),
                        contentDescription = stringResource(R.string.label_name)
                    )
                },
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            val (emailValue, onEmailChanged) = email
            OutlinedTextField(
                value = emailValue ?: "",
                onValueChange = {
                    onEmailChanged(it.take(40))
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.label_email))
                },
                leadingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.rounded_alternate_email_24),
                        contentDescription = stringResource(R.string.label_email)
                    )
                },
                singleLine = true
            )
        }
    }
}


@Previews
@Composable
private fun InstructorEditPreview() {
    AppTheme {
        InstructorEditLayout(
            isEditMode = true,
            snackbarHostState = SnackbarHostState(),
            canBeSubmitted = remember { mutableStateOf(false) },
            image = remember { mutableStateOf(null) },
            name = remember { mutableStateOf("Ivanov Ivan Ivanovich") },
            email = remember { mutableStateOf(null) }
        )
    }
}