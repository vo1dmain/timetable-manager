package ru.vo1dmain.timetables.settings.appearance

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.settings.R
import ru.vo1dmain.timetables.settings.components.FlatListPreference
import ru.vo1dmain.timetables.settings.components.SwitchPreference
import ru.vo1dmain.timetables.ui.TopBarScaffoldScreen

@Composable
internal fun AppearanceScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {
    val viewModel = viewModel<AppearanceViewModel>()
    
    val themeState = viewModel.theme.collectAsStateWithLifecycle()
    val dynamicColorState = viewModel.dynamicColor.collectAsStateWithLifecycle()
    
    AppearanceLayout(
        snackbarHostState = snackbarHostState,
        themeState = themeState,
        dynamicColorState = dynamicColorState,
        onNavigateUp = onNavigateUp,
        onThemeSelected = { viewModel.selectTheme(it) },
        onDynamicColorToggled = { viewModel.toggleDynamicColor(it) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppearanceLayout(
    snackbarHostState: SnackbarHostState,
    themeState: State<String>,
    dynamicColorState: State<Boolean>,
    onNavigateUp: () -> Unit = {},
    onThemeSelected: (String) -> Unit = {},
    onDynamicColorToggled: (Boolean) -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(topAppBarState)
    val scrollState = rememberScrollState()
    
    val themeEntries = stringArrayResource(R.array.theme_entries)
    val themeValues = stringArrayResource(R.array.theme_values)
    
    val entries = remember { themeValues.zip(themeEntries).toMap() }
    
    TopBarScaffoldScreen(
        title = stringResource(R.string.screen_title_appearance),
        snackbarHostState = snackbarHostState,
        topAppBarState = topAppBarState,
        scrollBehavior = scrollBehavior,
        scrollState = scrollState,
        onNavigateUp = onNavigateUp
    ) {
        FlatListPreference(
            title = stringResource(R.string.preference_title_theme),
            state = themeState,
            entries = entries,
            onValueChanged = onThemeSelected
        )
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            Spacer(modifier = Modifier.height(8.dp))
            
            SwitchPreference(
                title = stringResource(R.string.preference_title_dynamic_color),
                state = dynamicColorState,
                onCheckedChange = onDynamicColorToggled
            )
        }
    }
}


@Preview(group = "Default")
@Preview(group = "Default", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    AppTheme {
        val themeState = remember { mutableStateOf("System") }
        val dynamicColorState = remember { mutableStateOf(false) }
        
        AppearanceLayout(
            SnackbarHostState(),
            themeState = themeState,
            dynamicColorState = dynamicColorState
        )
    }
}
