package com.sweatnote.app.ui.screens.workout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.sweatnote.app.data.LiveWorkoutExercise
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.LiveWorkoutViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveWorkoutScreen() {
    val liveWorkoutViewModel: LiveWorkoutViewModel = viewModel(
        factory = AppViewModelProvider.Factory
    )

    val uiState by liveWorkoutViewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Live Workout") })
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = uiState,
                key = {it.exercise.id}
            ){ liveExercise ->
                WorkoutExerciseCard(liveExercise = liveExercise)
            }
        }
    }
}

@Composable
fun WorkoutExerciseCard(liveExercise: LiveWorkoutExercise) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = liveExercise.exercise.name,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "Sets will be logged here.",
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}
