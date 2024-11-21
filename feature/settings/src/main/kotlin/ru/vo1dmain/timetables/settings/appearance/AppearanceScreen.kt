package ru.vo1dmain.timetables.settings.appearance

import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
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
import ru.vo1dmain.timetables.ui.NavigationIcon

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
    
    val themeEntries = stringArrayResource(R.array.theme_entries)
    val themeValues = stringArrayResource(R.array.theme_values)
    
    val entries = remember { themeValues.zip(themeEntries).toMap() }
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            LargeTopAppBar(
                scrollBehavior = scrollBehavior,
                title = {
                    Text(text = stringResource(R.string.preference_category_title_appearance))
                },
                navigationIcon = {
                    NavigationIcon { onNavigateUp() }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .fillMaxSize()
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
}


@Preview(group = "Default")
@Preview(group = "Default", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PrefsScreenPreview() {
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
