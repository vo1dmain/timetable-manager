package ru.vo1dmain.timetables.teachers.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.shapes
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.rememberAsyncImagePainter
import ru.vo1dmain.timetables.data.models.Teacher
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.design.dimensions
import ru.vo1dmain.timetables.teachers.R
import ru.vo1dmain.timetables.teachers.TeacherEdit
import ru.vo1dmain.timetables.teachers.TeacherScreen
import ru.vo1dmain.timetables.ui.Previews
import ru.vo1dmain.timetables.ui.R as UiR

@Composable
internal fun TeachersListScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit
) {
    val viewModel = viewModel<TeachersListViewModel>()
    val items = viewModel.all.collectAsStateWithLifecycle()
    
    TeachersListLayout(
        items = items,
        snackbarHostState = snackbarHostState,
        onTeacherClick = { onNavigateTo(TeacherScreen(it)) },
        onTeacherLongClick = {
            viewModel.toggleSelection(it)
        },
        onTeacherImageClick = {
            viewModel.toggleSelection(it)
        },
        onClickAddTeacher = { onNavigateTo(TeacherEdit()) },
        isItemSelected = { viewModel.isSelected(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeachersListLayout(
    items: State<List<Teacher>>,
    snackbarHostState: SnackbarHostState,
    onTeacherClick: (Int) -> Unit = {},
    onTeacherLongClick: (Int) -> Unit = {},
    onTeacherImageClick: (Int) -> Unit = {},
    onClickAddTeacher: () -> Unit = {},
    isItemSelected: (Int) -> Boolean = { false }
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    var searchBarOpenState by remember { mutableStateOf(false) }
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(R.string.screen_title_teachers))
                },
                actions = {
                    IconButton(onClick = { searchBarOpenState = true }) {
                        Icon(
                            painter = painterResource(R.drawable.rounded_search_24),
                            contentDescription = stringResource(UiR.string.action_search)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = onClickAddTeacher) {
                Icon(imageVector = Icons.Rounded.Add, contentDescription = stringResource(R.string.action_add_teacher))
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(dimensions.contentPadding)
        ) {
            val teachers = items.value
            
            itemsIndexed(
                items = teachers,
                key = { index, item -> item.id }
            ) { index, item ->
                TeacherListItem(
                    item = item,
                    isSelected = isItemSelected(item.id),
                    onClick = onTeacherClick,
                    onLongClick = onTeacherLongClick,
                    onImageClick = onTeacherImageClick
                )
                
                if (index != teachers.size - 1) {
                    Spacer(modifier = Modifier.height(dimensions.smallSpacerSize))
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeacherListItem(
    item: Teacher,
    isSelected: Boolean = false,
    onClick: (Int) -> Unit = {},
    onLongClick: (Int) -> Unit = {},
    onImageClick: (Int) -> Unit = {}
) {
    val colors = if (isSelected) {
        ListItemDefaults.colors(containerColor = colorScheme.secondaryContainer)
    } else {
        ListItemDefaults.colors()
    }
    
    ListItem(
        modifier = Modifier
            .clip(shapes.medium)
            .combinedClickable(
                onLongClick = { onLongClick(item.id) },
                onClick = { onClick(item.id) }
            ),
        supportingContent = {
            val title = item.title
            if (title != null) Text(text = title)
        },
        headlineContent = { Text(text = item.name) },
        leadingContent = {
            val modifier = Modifier
                .clickable {
                    onImageClick(item.id)
                }
                .clip(shapes.small)
                .requiredSize(dimensions.smallImageSize)
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = "Selection icon",
                    modifier = modifier.background(colorScheme.onSecondaryContainer),
                    tint = colorScheme.secondaryContainer
                )
            } else {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = item.image,
                        fallback = painterResource(R.drawable.rounded_person_24_filled)
                    ),
                    contentDescription = "Image",
                    modifier = modifier.background(colorScheme.secondaryContainer)
                )
            }
        },
        colors = colors
    )
}

@Previews
@Composable
private fun Preview() {
    val state = remember {
        mutableStateOf(
            (1..15).map {
                Teacher(
                    id = it,
                    name = "Ivanov Ivan Ivanovich",
                    title = if (it % 2 == 1) "Programming" else "Databases",
                    email = "sample@mail.com"
                )
            }
        )
    }
    
    var selected by remember { mutableStateOf(setOf(1, 3, 5, 7, 9, 11)) }
    
    fun toggleSelected(id: Int) {
        if (selected.contains(id)) selected -= id
        else selected += id
    }
    
    AppTheme {
        TeachersListLayout(
            items = state,
            snackbarHostState = SnackbarHostState(),
            onTeacherLongClick = {
                toggleSelected(it)
            },
            onTeacherImageClick = {
                toggleSelected(it)
            },
            isItemSelected = { selected.contains(it) }
        )
    }
}