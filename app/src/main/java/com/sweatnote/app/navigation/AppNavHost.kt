package com.sweatnote.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sweatnote.app.ui.screens.DashboardScreen
import com.sweatnote.app.ui.screens.HistoryScreen
import com.sweatnote.app.ui.screens.ProfileScreen
import com.sweatnote.app.ui.screens.RoutinesScreen
import com.sweatnote.app.ui.screens.exercise.ExerciseListScreen
import com.sweatnote.app.ui.screens.workout.LiveWorkoutScreen
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.LiveWorkoutViewModel

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier){
    NavHost(navController = navController, startDestination = Screen.Dashboard.route, modifier = modifier){

        composable(Screen.Dashboard.route){
            DashboardScreen(navController = navController)
        }
        composable(Screen.History.route){
            HistoryScreen()
        }
        composable(route = Screen.Routines.route){
            RoutinesScreen(navController = navController)
        }
        composable(Screen.Profile.route){
            ProfileScreen()
        }
        composable(route = com.sweatnote.app.navigation.ExerciseListScreen.routeForWorkout) {
            ExerciseListScreen(navController = navController, mode = "workout")
        }

        composable(route = com.sweatnote.app.navigation.ExerciseListScreen.routeForRoutine){
            ExerciseListScreen(navController = navController, mode = "routine")
        }

        composable(
            route = com.sweatnote.app.navigation.LiveWorkoutScreen.routeWithArgs,
            arguments = listOf(navArgument(com.sweatnote.app.navigation.LiveWorkoutScreen.exerciseIdsArg) {
                type = NavType.StringType })
        ) { backStackEntry ->
            val liveWorkoutViewModel: LiveWorkoutViewModel = viewModel(
                factory = AppViewModelProvider.Factory,
                viewModelStoreOwner = backStackEntry
            )
            LiveWorkoutScreen(
                navController = navController,
                viewModel = liveWorkoutViewModel
            )
        }
    }
}