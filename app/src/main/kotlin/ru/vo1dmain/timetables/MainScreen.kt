package ru.vo1dmain.timetables

import android.annotation.SuppressLint
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.vo1dmain.timetables.calendar.CalendarNavigation
import ru.vo1dmain.timetables.calendar.calendarNavigation
import ru.vo1dmain.timetables.design.AppTheme
import ru.vo1dmain.timetables.settings.SettingsNavigation
import ru.vo1dmain.timetables.settings.settingsNavigation
import ru.vo1dmain.timetables.subjects.SubjectsNavigation
import ru.vo1dmain.timetables.subjects.subjectsNavigation
import ru.vo1dmain.timetables.teachers.TeachersNavigation
import ru.vo1dmain.timetables.teachers.teachersNavigation
import ru.vo1dmain.timetables.ui.Previews
import ru.vo1dmain.timetables.calendar.R as CalendarR
import ru.vo1dmain.timetables.settings.R as SettingsR
import ru.vo1dmain.timetables.subjects.R as SubjectsR
import ru.vo1dmain.timetables.teachers.R as TeachersR

private data class TopLevelRoute(
    @StringRes val name: Int,
    @DrawableRes val idleIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: Any
)

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val snackbarHostState = remember { SnackbarHostState() }
    val topLevelRoutes = remember {
        listOf<TopLevelRoute>(
            TopLevelRoute(
                name = CalendarR.string.screen_title_calendar,
                idleIcon = CalendarR.drawable.rounded_event_note_24,
                selectedIcon = CalendarR.drawable.rounded_event_note_24_filled,
                route = CalendarNavigation
            ),
            TopLevelRoute(
                name = SubjectsR.string.screen_title_subjects,
                idleIcon = SubjectsR.drawable.rounded_library_books_24,
                selectedIcon = SubjectsR.drawable.rounded_library_books_24_filled,
                route = SubjectsNavigation
            ),
            TopLevelRoute(
                name = TeachersR.string.screen_title_teachers,
                idleIcon = TeachersR.drawable.rounded_group_24,
                selectedIcon = TeachersR.drawable.rounded_group_24_filled,
                route = TeachersNavigation
            ),
            TopLevelRoute(
                name = SettingsR.string.screen_title_settings,
                idleIcon = SettingsR.drawable.rounded_settings_24,
                selectedIcon = SettingsR.drawable.rounded_settings_24_filled,
                route = SettingsNavigation
            )
        )
    }
    
    AppTheme {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    topLevelRoutes = topLevelRoutes,
                    navController = navController
                )
            }
        ) { paddingValues ->
            TopLevelNavHost(
                navController = navController,
                snackbarHostState = snackbarHostState,
                modifier = Modifier.padding(paddingValues)
            )
        }
    }
}

@SuppressLint("RestrictedApi")
@Composable
private fun NavigationBar(
    topLevelRoutes: List<TopLevelRoute>,
    navController: NavHostController
) {
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        
        topLevelRoutes.forEach { route ->
            val name = stringResource(route.name)
            
            val selected by derivedStateOf {
                val currentDestination = navBackStackEntry?.destination
                currentDestination?.hierarchy?.any { it.hasRoute(route.route::class) } == true
            }
            
            NavigationBarItem(
                icon = {
                    val icon = if (selected) route.selectedIcon else route.idleIcon
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = name
                    )
                },
                label = { Text(text = name) },
                selected = selected,
                onClick = {
                    navController.navigate(route.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}


@Composable
private fun TopLevelNavHost(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = CalendarNavigation,
        modifier = modifier
    ) {
        calendarNavigation(
            snackbarHostState = snackbarHostState,
            onNavigateTo = { navController.navigate(it) },
            onNavigateUp = { navController.navigateUp() }
        )
        
        subjectsNavigation(
            snackbarHostState = snackbarHostState,
            onNavigateTo = { navController.navigate(it) },
            onNavigateUp = { navController.navigateUp() }
        )
        
        teachersNavigation(
            snackbarHostState = snackbarHostState,
            onNavigateTo = { navController.navigate(it) },
            onNavigateUp = { navController.navigateUp() }
        )
        
        settingsNavigation(
            snackbarHostState = snackbarHostState,
            onNavigateTo = { navController.navigate(it) },
            onNavigateUp = { navController.navigateUp() }
        )
    }
}

@Previews
@Composable
private fun Preview() {
    AppTheme {
        MainScreen()
    }
}