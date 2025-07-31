package com.sweatnote.app.ui.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sweatnote.app.data.LiveWorkoutExercise
import com.sweatnote.app.data.WorkoutSet
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.LiveWorkoutViewModel
import kotlinx.coroutines.flow.collect


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveWorkoutScreen(navController: NavController, viewModel: LiveWorkoutViewModel) {
//    val liveWorkoutViewModel: LiveWorkoutViewModel = viewModel(
//        factory = AppViewModelProvider.Factory
//    )

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.navigateBack.collect{
            navController.navigate(com.sweatnote.app.navigation.Screen.Dashboard.route){
                popUpTo(navController.graph.id){
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Live Workout") },
                actions = {
                    TextButton(
                        onClick = {viewModel.finishWorkout()},
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Done, contentDescription = "Finish Workout")
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Finish")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = uiState,
                key = {it.exercise.id}
            ){ liveExercise ->
                WorkoutExerciseCard(
                    liveExercise = liveExercise,
                    onWeightChanged = {setId, newWeight ->
                        viewModel.onWeightChanged(liveExercise.exercise.id, setId, newWeight)
                    },
                    onRepsChanged = { setId, newReps ->
                        viewModel.onRepsChanged(liveExercise.exercise.id, setId, newReps)
                    },
                    onAddSet = {
                        viewModel.addSet(liveExercise.exercise.id)
                    })
            }
        }
    }
}

@Composable
fun WorkoutExerciseCard(
    liveExercise: LiveWorkoutExercise,
    onWeightChanged: (setId: java.util.UUID, newWeight: String) -> Unit,
    onRepsChanged: (setId: java.util.UUID, newReps: String) -> Unit,
    onAddSet: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Column (
            modifier = Modifier.padding(16.dp)
        ){
            Text(
                text = liveExercise.exercise.name,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            liveExercise.sets.forEachIndexed { index, set ->
                SetInputRow(
                    set = set,
                    setNumber = index + 1,
                    onWeightChange = { newWeight -> onWeightChanged(set.id, newWeight) },
                    onRepsChange = { newReps -> onRepsChanged(set.id, newReps) }
                )
            }

            Button(
                onClick = onAddSet,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Set")
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add Set")
            }
        }
    }
}

@Composable
fun SetInputRow(
    set: WorkoutSet,
    setNumber: Int,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "$setNumber",
            modifier = Modifier.weight(0.5f),
            style = MaterialTheme.typography.bodyLarge
        )

        OutlinedTextField(
            value = set.weight,
            onValueChange = onWeightChange,
            label = { Text("kg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(2f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = set.reps,
            onValueChange = onRepsChange,
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(2f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = {},
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Check, contentDescription = "Mark set complete")
        }
    }
}
