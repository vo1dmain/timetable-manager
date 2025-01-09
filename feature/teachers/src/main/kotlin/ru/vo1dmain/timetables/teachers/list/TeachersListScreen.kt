package ru.vo1dmain.timetables.teachers.list

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
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
        onClickTeacher = { onNavigateTo(TeacherScreen(it)) },
        onClickAddTeacher = { onNavigateTo(TeacherEdit()) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TeachersListLayout(
    items: State<List<Teacher>>,
    snackbarHostState: SnackbarHostState,
    onClickTeacher: (Int) -> Unit = {},
    onClickAddTeacher: () -> Unit = {}
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
                .fillMaxSize()
        ) {
            items(
                items = items.value,
                key = { it.id }
            ) {
                TeacherListItem(
                    item = it,
                    onClick = onClickTeacher
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun TeacherListItem(
    item: Teacher,
    onClick: (Int) -> Unit = {}
) {
    ListItem(
        modifier = Modifier
            .combinedClickable(
                onLongClick = {},
                onClick = { onClick(item.id) }
            ),
        supportingContent = {
            val title = item.title
            if (title != null) Text(text = title)
        },
        headlineContent = { Text(text = item.name) },
        leadingContent = {
            Image(
                painter = rememberAsyncImagePainter(
                    model = item.image,
                    fallback = painterResource(R.drawable.rounded_person_24_filled)
                ),
                contentDescription = "Image",
                modifier = Modifier
                    .clip(shapes.small)
                    .requiredSize(dimensions.smallImageSize)
                    .background(colorScheme.secondaryContainer)
            )
        }
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
                    title = if (it % 2 == 1) "Programming" else null,
                    email = "sample@mail.com"
                )
            }
        )
    }
    
    AppTheme {
        TeachersListLayout(
            items = state,
            snackbarHostState = SnackbarHostState()
        )
    }
}