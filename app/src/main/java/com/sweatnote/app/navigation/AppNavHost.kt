package com.sweatnote.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sweatnote.app.ui.screens.DashboardScreen
import com.sweatnote.app.ui.screens.ExerciseListScreen
import com.sweatnote.app.ui.screens.HistoryScreen
import com.sweatnote.app.ui.screens.ProfileScreen
import com.sweatnote.app.ui.screens.RoutinesScreen

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
        composable("exercise_list") {
            ExerciseListScreen()
        }
    }
}