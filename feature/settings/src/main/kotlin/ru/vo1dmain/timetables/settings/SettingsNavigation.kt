package ru.vo1dmain.timetables.settings

import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import ru.vo1dmain.timetables.settings.about.AboutScreen
import ru.vo1dmain.timetables.settings.appearance.AppearanceScreen

@Serializable
internal object Settings

@Serializable
internal object About

@Serializable
internal object Appearance

@Serializable
object SettingsNavigation

fun NavGraphBuilder.settingsNavigation(
    snackbarHostState: SnackbarHostState,
    onNavigateTo: (Any) -> Unit,
    onNavigateUp: () -> Unit
) {
    navigation<SettingsNavigation>(startDestination = Settings) {
        composable<Settings> {
            SettingsScreen(snackbarHostState, onNavigateTo)
        }
        
        composable<Appearance> {
            AppearanceScreen(snackbarHostState, onNavigateUp)
        }
        
        composable<About> {
            AboutScreen(snackbarHostState, onNavigateUp)
        }
    }
}