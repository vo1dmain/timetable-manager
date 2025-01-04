package ru.vo1dmain.timetables.settings

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.ui.Previews


@Composable
internal fun SettingsScreen(
    snackbarHostState: SnackbarHostState,
    onNavigate: (Any) -> Unit
) {
    val categories = remember { categories }
    SettingsLayout(snackbarHostState, categories, onNavigate)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsLayout(
    snackbarHostState: SnackbarHostState,
    categories: List<Preference<out Any>>,
    onNavigate: (Any) -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(R.string.screen_title_settings))
                },
                scrollBehavior = scrollBehavior
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            categories.forEach {
                ListItem(
                    modifier = Modifier.clickable(onClick = { onNavigate(it.route) }),
                    headlineContent = { Text(text = stringResource(it.title)) },
                    leadingContent = {
                        Icon(
                            painter = painterResource(it.icon),
                            contentDescription = null
                        )
                    }
                )
            }
        }
    }
}

private data class Preference<T>(
    @StringRes val title: Int,
    @DrawableRes val icon: Int,
    val route: T
)

private val categories = listOf(
    Preference(
        R.string.screen_title_appearance,
        R.drawable.rounded_palette_24,
        Appearance
    ),
    Preference(
        R.string.screen_title_about,
        R.drawable.rounded_info_24,
        About
    )
)

@Previews
@Composable
private fun Preview() {
    AppTheme {
        SettingsLayout(SnackbarHostState(), categories)
    }
}