package com.sweatnote.app.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sweatnote.app.data.RoutineWithExercises
import com.sweatnote.app.navigation.LiveWorkoutScreen
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.RoutinesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutinesScreen(
    navController: NavController,
    viewModel: RoutinesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val routines by viewModel.routines.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Workout Routines") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(com.sweatnote.app.navigation.ExerciseListScreen.routeForRoutine)
            }) {
                Icon(Icons.Default.Add, contentDescription = "Create Routine")
            }
        }
    ){ innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)){
            if (routines.isEmpty()){
                item{
                    Text(
                        text = "No routine created yet. Tap the '+' button to create one!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }else{
                items(routines){routine ->
                    RoutineCard(routine = routine, navController = navController)
                }
            }
        }
    }
}

@Composable
fun RoutineCard(routine: RoutineWithExercises, navController: NavController){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                val exerciseIds = routine.exercises.map { it.exerciseId }.joinToString(",")
                if(exerciseIds.isNotEmpty()){
                    navController.navigate("${LiveWorkoutScreen.route}/$exerciseIds")
                }
            }
    ){
        Column(modifier = Modifier.padding(16.dp)){
            Row(verticalAlignment = Alignment.CenterVertically){
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Routines",
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = routine.routine.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${routine.exercises.size} exercises",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}