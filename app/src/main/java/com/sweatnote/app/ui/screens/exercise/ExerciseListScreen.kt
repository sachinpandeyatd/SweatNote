package com.sweatnote.app.ui.screens.exercise

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sweatnote.app.data.Exercise
import com.sweatnote.app.navigation.LiveWorkoutScreen
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.ExerciseListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    navController: NavController,
    viewModel: ExerciseListViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val exerciseList by viewModel.exerciseList.collectAsState()
    val selectedIds by viewModel.selectedExerciseIds.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Select Exercises")})
        },
        floatingActionButton = {
            if(selectedIds.isNotEmpty()){
                FloatingActionButton(onClick = {
                    val ids = selectedIds.joinToString(separator = ",")
                    navController.navigate("${LiveWorkoutScreen.route}/$ids")
                }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Start Workout")
                }
            }
        }
    ) { innerPadding -> LazyColumn (
            modifier = Modifier.padding(innerPadding)
        ) {
            items(exerciseList) { exercise ->
                val isSelected = exercise.id in selectedIds
                ExerciseListItem(
                    exercise = exercise,
                    isSelected = isSelected,
                    onItemClick = {viewModel.toggleExerciseSelection(exercise.id)}
                )
            }
        }
    }
}

@Composable
fun ExerciseListItem(exercise: Exercise, isSelected: Boolean, onItemClick: () -> Unit){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp).clickable { onItemClick() },
        colors = CardDefaults.cardColors(containerColor =
            if(isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceVariant,)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Muscle: ${exercise.primaryMuscle}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}