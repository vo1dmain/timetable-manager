package ru.vo1dmain.timetables.teachers.edit

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.design.dimensions
import ru.vo1dmain.timetables.teachers.R
import ru.vo1dmain.timetables.ui.Previews
import ru.vo1dmain.timetables.ui.TopBarScaffoldScreen
import ru.vo1dmain.timetables.ui.R as UiR

@Composable
internal fun TeacherEditScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {
    val viewModel = viewModel<TeacherEditViewModel>()
    
    TeacherEditLayout(
        isCreationMode = viewModel.isCreationMode,
        state = viewModel.state,
        snackbarHostState = snackbarHostState,
        onNavigationIconClick = onNavigateUp,
        onPickImage = {
            viewModel.savePhoto(it)
        },
        onSubmit = {
            viewModel.submit()
            onNavigateUp()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeacherEditLayout(
    isCreationMode: Boolean,
    state: EditScreenState,
    snackbarHostState: SnackbarHostState,
    onNavigationIconClick: () -> Unit = {},
    onPickImage: (Uri) -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()
    
    val pickMedia = rememberLauncherForActivityResult(PickVisualMedia()) {
        if (it != null) {
            onPickImage(it)
        }
    }
    
    val screenTitle = remember {
        if (isCreationMode) R.string.action_add_teacher
        else UiR.string.action_edit
    }
    
    TopBarScaffoldScreen(
        title = stringResource(screenTitle),
        snackbarHostState = snackbarHostState,
        contentModifier = Modifier.padding(dimensions.contentPadding),
        topAppBarState = topAppBarState,
        scrollBehavior = scrollBehavior,
        scrollState = scrollState,
        onNavigationIconClick = onNavigationIconClick,
        actions = {
            IconButton(
                onClick = onSubmit,
                enabled = state.canBeSubmitted
            ) {
                Icon(
                    painter = painterResource(R.drawable.rounded_save_24),
                    contentDescription = stringResource(UiR.string.action_save)
                )
            }
        }
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
                .requiredSize(dimensions.largeImageSize)
                .background(colorScheme.secondaryContainer)
        )
        
        Spacer(modifier = Modifier.height(dimensions.smallSpacerSize))
        
        OutlinedButton(
            onClick = { pickMedia.launch(PickVisualMediaRequest(PickVisualMedia.ImageOnly)) },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.action_edit_photo))
        }
        
        Spacer(modifier = Modifier.height(dimensions.smallSpacerSize))
        
        Text(
            text = stringResource(R.string.label_general),
            style = typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(dimensions.smallSpacerSize))
        
        SingleLineTextField(
            state = state.name,
            label = stringResource(R.string.label_name),
            limit = maxNameLength
        )
        
        Spacer(modifier = Modifier.height(dimensions.smallSpacerSize))
        
        SingleLineTextField(
            state = state.title,
            label = stringResource(R.string.label_title),
            limit = maxNameLength
        )
        
        Spacer(modifier = Modifier.height(dimensions.mediumSpacerSize))
        
        Text(
            text = stringResource(R.string.label_contacts),
            style = typography.titleSmall
        )
        
        Spacer(modifier = Modifier.height(dimensions.smallSpacerSize))
        
        SingleLineTextField(
            state = state.email,
            label = stringResource(R.string.label_email),
            limit = maxEmailLength
        )
    }
}

@Composable
private fun SingleLineTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    label: String? = null,
    icon: Painter? = null,
    limit: Int? = null
) {
    OutlinedTextField(
        value = state.text.toString(),
        onValueChange = {
            val input = if (limit != null) it.take(limit) else it
            state.setTextAndPlaceCursorAtEnd(input)
        },
        modifier = modifier
            .sizeIn(maxWidth = textFieldMaxWidth)
            .fillMaxWidth(),
        label = if (label == null) null else {
            {
                Text(text = label)
            }
        },
        leadingIcon = if (icon == null) null else {
            {
                Icon(
                    painter = icon,
                    contentDescription = label ?: "Icon"
                )
            }
        },
        singleLine = true
    )
}


@Previews
@Composable
private fun Preview() {
    val state = remember {
        EditScreenState(
            name = TextFieldState("Ivanov Ivan Ivanovich"),
            title = TextFieldState("Programming"),
            email = TextFieldState("sample@mail.com")
        )
    }
    AppTheme {
        TeacherEditLayout(
            isCreationMode = true,
            snackbarHostState = SnackbarHostState(),
            state = state
        )
    }
}

private val textFieldMaxWidth = 488.dp

private const val maxNameLength = Short.MAX_VALUE.toInt()
private const val maxEmailLength = 320