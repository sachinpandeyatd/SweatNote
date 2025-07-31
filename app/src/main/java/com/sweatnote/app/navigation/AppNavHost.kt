package com.sweatnote.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sweatnote.app.ui.screens.DashboardScreen
import com.sweatnote.app.ui.screens.HistoryScreen
import com.sweatnote.app.ui.screens.ProfileScreen
import com.sweatnote.app.ui.screens.RoutinesScreen

import com.sweatnote.app.ui.screens.exercise.ExerciseListScreen as ExerciseListComposable
import com.sweatnote.app.ui.screens.workout.LiveWorkoutScreen as LiveWorkoutComposable

import com.sweatnote.app.navigation.ExerciseListScreen as ExerciseListRoute
import com.sweatnote.app.navigation.LiveWorkoutScreen as LiveWorkoutRoute

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier){
    NavHost(navController = navController, startDestination = Screen.Dashboard.route, modifier = modifier){
        composable(Screen.Dashboard.route){
            DashboardScreen(navController = navController)
        }
        composable(Screen.History.route){
            HistoryScreen()
        }
        composable(Screen.Routines.route){
            RoutinesScreen()
        }
        composable(Screen.Profile.route){
            ProfileScreen()
        }
        composable(ExerciseListRoute.route) {
            ExerciseListComposable(navController = navController)
        }

        composable(
            route = LiveWorkoutRoute.routeWithArgs,
            arguments = listOf(navArgument(LiveWorkoutRoute.exerciseIdsArg) { type = NavType.StringType })
        ) {
            LiveWorkoutComposable()
        }
    }
}