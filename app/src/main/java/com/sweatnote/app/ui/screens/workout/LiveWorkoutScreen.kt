package com.sweatnote.app.ui.screens.workout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveWorkoutScreen(navController: NavController, viewModel: LiveWorkoutViewModel) {

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
                    },
                    onToggleSetComplete = {setId ->
                        viewModel.toggleSetCompletion(liveExercise.exercise.id, setId)
                    },
                    onDeleteSet = { setId ->
                        viewModel.deleteSet(liveExercise.exercise.id, setId)
                    })
            }
        }
    }
}

@Composable
fun WorkoutExerciseCard(
    liveExercise: LiveWorkoutExercise,
    onWeightChanged: (setId: UUID, newWeight: String) -> Unit,
    onRepsChanged: (setId: UUID, newReps: String) -> Unit,
    onAddSet: () -> Unit,
    onToggleSetComplete: (setId: UUID) -> Unit,
    onDeleteSet: (setId: UUID) -> Unit) {
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
                val previousSet = liveExercise.previousPerformance.getOrNull(index)
                val previousInfo = previousSet?.let { "${it.weight}kg x ${it.reps}" } ?: "-/-"
                SetInputRow(
                    set = set,
                    setNumber = index + 1,
                    previousSetInfo = previousInfo,
                    onWeightChange = { newWeight -> onWeightChanged(set.id, newWeight) },
                    onRepsChange = { newReps -> onRepsChanged(set.id, newReps) },
                    onToggleComplete = { onToggleSetComplete(set.id) },
                    onDeleteSet = { onDeleteSet(set.id) }
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
    previousSetInfo: String,
    onWeightChange: (String) -> Unit,
    onRepsChange: (String) -> Unit,
    onToggleComplete: () -> Unit,
    onDeleteSet: () -> Unit
) {
    val isCompleted = set.isCompleted
    val colors = if(isCompleted){
        OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }else{
        OutlinedTextFieldDefaults.colors()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onDeleteSet, modifier = Modifier.size(24.dp)) {
            Icon(
                Icons.Default.Close,
                contentDescription = "Delete Set",
                tint = MaterialTheme.colorScheme.error
            )
        }
        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(0.7f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$setNumber",
                modifier = Modifier.weight(0.5f),
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = previousSetInfo,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        OutlinedTextField(
            value = set.weight,
            onValueChange = onWeightChange,
            label = { Text("kg") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(2f),
            enabled = !isCompleted,
            colors = colors
        )

        Spacer(modifier = Modifier.width(8.dp))

        OutlinedTextField(
            value = set.reps,
            onValueChange = onRepsChange,
            label = { Text("Reps") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.weight(2f),
            enabled = !isCompleted,
            colors = colors
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconToggleButton(
            checked = isCompleted,
            onCheckedChange = {onToggleComplete()},
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "Mark set complete",
                tint = if(isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
