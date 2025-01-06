package ru.vo1dmain.timetables.settings.about

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.design.dimensions
import ru.vo1dmain.timetables.settings.R
import ru.vo1dmain.timetables.ui.TopBarScaffoldScreen
import ru.vo1dmain.timetables.ui.mipmapPainterResource
import ru.vo1dmain.timetables.design.R as DesignR

@Composable
internal fun AboutScreen(
    snackbarHostState: SnackbarHostState,
    onNavigateUp: () -> Unit
) {
    val viewModel = viewModel<AboutViewModel>()
    AboutScreenLayout(
        versionCode = viewModel.versionCode,
        versionName = viewModel.versionName,
        snackbarHostState = snackbarHostState,
        onNavigationIconClick = onNavigateUp
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AboutScreenLayout(
    snackbarHostState: SnackbarHostState,
    versionName: String,
    versionCode: Number,
    onNavigationIconClick: () -> Unit = {},
    onShowPatchNotes: () -> Unit = {}
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topAppBarState)
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    
    TopBarScaffoldScreen(
        title = stringResource(R.string.screen_title_about),
        snackbarHostState = snackbarHostState,
        topAppBarState = topAppBarState,
        scrollBehavior = scrollBehavior,
        scrollState = scrollState,
        onNavigationIconClick = onNavigationIconClick
    ) {
        AppInfo(versionName, versionCode) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(
                    message = it,
                    duration = SnackbarDuration.Long
                )
            }
        }
        
        HorizontalDivider(Modifier.padding(horizontal = dimensions.mediumSpacerSize))
        ListItem(
            modifier = Modifier.clickable(onClick = onShowPatchNotes),
            headlineContent = { Text(stringResource(R.string.action_title_whats_new)) }
        )
    }
}

@Composable
private fun AppInfo(
    versionName: String,
    versionCode: Number,
    onEasterEggTrigger: (String) -> Unit = {}
) {
    var easterEggCounter by remember { mutableIntStateOf(0) }
    val easterEggMessage = stringResource(R.string.message_easter_egg)
    
    val onEasterEggClick = {
        easterEggCounter++
        
        if (easterEggCounter == 5) {
            onEasterEggTrigger(easterEggMessage)
            easterEggCounter = 0
        }
    }
    
    Column(
        modifier = Modifier
            .clickable(onClick = onEasterEggClick)
            .fillMaxWidth()
            .padding(vertical = dimensions.contentPadding),
        verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            tint = Color.Unspecified,
            painter = mipmapPainterResource(DesignR.mipmap.ic_launcher),
            contentDescription = null,
            modifier = Modifier
                .size(108.dp)
                .clip(RoundedCornerShape(20.dp))
        )
        Text(
            text = stringResource(DesignR.string.app_name),
            style = typography.titleLarge
        )
        Text(
            text = stringResource(R.string.app_version, versionName, versionCode),
            style = typography.bodySmall
        )
    }
}


@Preview(group = "Default")
@Preview(group = "Default", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun Preview() {
    AppTheme {
        AboutScreenLayout(SnackbarHostState(), "2.0.0", 4)
    }
}
