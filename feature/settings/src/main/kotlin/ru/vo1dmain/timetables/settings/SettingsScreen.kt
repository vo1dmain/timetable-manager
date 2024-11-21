package ru.vo1dmain.timetables.settings

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ru.vo1dmain.timetables.design.AppTheme


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
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(R.string.fragment_label_settings))
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            categories.forEach {
                ListItem(
                    modifier = Modifier.clickable(onClick = { onNavigate(it.route) }),
                    headlineContent = { Text(stringResource(it.title)) },
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
        R.string.preference_category_title_appearance,
        R.drawable.rounded_palette_24,
        Appearance
    ),
    Preference(
        R.string.fragment_label_about,
        R.drawable.rounded_info_24,
        About
    )
)

@Preview(group = "Default")
@Preview(group = "Default", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PrefsScreenPreview() {
    AppTheme {
        SettingsLayout(SnackbarHostState(), categories)
    }
}