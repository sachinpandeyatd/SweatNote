package com.sweatnote.app.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Dashboard", Icons.Default.Home)
    object History : Screen("history", "History", Icons.Default.DateRange)
    object Routines : Screen("routines", "Routines", Icons.Default.List)
    object Profile : Screen("profile", "Profile", Icons.Default.Person)
}

val bottomNavItems = listOf(
    Screen.Dashboard,
    Screen.History,
    Screen.Routines,
    Screen.Profile
)

object LiveWorkoutScreen {
    const val route = "live_workout"
    const val exerciseIdsArg = "exerciseIds" // The argument name as a constant
    val routeWithArgs = "$route/{$exerciseIdsArg}" // The full route with argument placeholder
}

object ExerciseListScreen{
    const val routeForWorkout = "exercise_list_for_workout"
    const val routeForRoutine = "exercise_list_for_routine"
}