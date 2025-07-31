package com.sweatnote.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sweatnote.app.data.Exercise
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.ExerciseListViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    viewModel: ExerciseListViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val exerciseList by viewModel.exerciseList.collectAsState()

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Exercises")})
        }
    ){
        innerPadding -> LazyColumn (
            modifier = Modifier.padding(innerPadding)
        ) {
            items(exerciseList) { exercise ->
                ExerciseListItem(exercise = exercise)
            }
        }
    }
}

@Composable
fun ExerciseListItem(exercise: Exercise){
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = exercise.name,
                style = androidx.compose.material3.MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Muscle: ${exercise.primaryMuscle}",
                style = androidx.compose.material3.MaterialTheme.typography.bodySmall
            )
        }
    }
}