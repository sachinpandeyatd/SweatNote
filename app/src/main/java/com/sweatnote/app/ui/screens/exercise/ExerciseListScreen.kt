package com.sweatnote.app.ui.screens.exercise

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sweatnote.app.data.Exercise
import com.sweatnote.app.data.ExerciseWithMuscles
import com.sweatnote.app.navigation.LiveWorkoutScreen
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.ExerciseListViewModel
import com.sweatnote.app.ui.viewmodels.RoutinesViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseListScreen(
    navController: NavController, mode: String,
    exerciseViewModel: ExerciseListViewModel = viewModel(factory = AppViewModelProvider.Factory),
    routinesViewModel: RoutinesViewModel = viewModel(factory = AppViewModelProvider.Factory)
){
    val exerciseList by exerciseViewModel.exerciseList.collectAsState()
    val selectedIds by exerciseViewModel.selectedExerciseIds.collectAsState()

    var showDialog by rememberSaveable { mutableStateOf(false) }
    var routineName by remember { mutableStateOf("") }

    if (showDialog){
        SaveRoutineDialog(
            routineName = routineName,
            onNameChange = { routineName = it },
            onDismiss = { showDialog = false },
            onSave = {
                val ids = selectedIds.toList()
                routinesViewModel.saveRoutine(routineName, ids)
                showDialog = false
                navController.popBackStack()
            }
        )
    }

    Scaffold (
        topBar = {
            TopAppBar(title = {Text("Select Exercises")})
        },
        floatingActionButton = {
            if(selectedIds.isNotEmpty()){
                FloatingActionButton(
                    onClick = {
                        if (mode == "workout") {
                            val ids = selectedIds.joinToString(separator = ",")
                            navController.navigate("${LiveWorkoutScreen.route}/$ids")
                        }else{
                            showDialog = true
                        }
                    }
                ) {
                    val icon = if (mode == "workout") Icons.Default.ArrowForward else Icons.Default.Send
                    Icon(icon, contentDescription = "Next")
                }
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = exerciseList,
                key = { item -> item.exercise.id }
            ) { exerciseWithMuscles ->
                val isSelected = exerciseWithMuscles.exercise.id in selectedIds
                ExerciseListItem(
                    exerciseWithMuscles = exerciseWithMuscles,
                    isSelected = isSelected,
                    onItemClick = { exerciseViewModel.toggleExerciseSelection(exerciseWithMuscles.exercise.id) }
                )
            }
        }
    }
}

@Composable
fun ExerciseListItem(exerciseWithMuscles: ExerciseWithMuscles, isSelected: Boolean, onItemClick: () -> Unit){
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
                text = exerciseWithMuscles.exercise.name,
                style = MaterialTheme.typography.titleLarge
            )
            exerciseWithMuscles.exercise.equipment?.let {
                Text(
                    text = "Equipment: $it",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (exerciseWithMuscles.primaryMuscles.isNotEmpty()) {
                Text(
                    text = "Primary: ${exerciseWithMuscles.primaryMuscles.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            if (exerciseWithMuscles.secondaryMuscles.isNotEmpty()) {
                Text(
                    text = "Secondary: ${exerciseWithMuscles.secondaryMuscles.joinToString { it.name }}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
fun SaveRoutineDialog(
    routineName: String,
    onNameChange: (String) -> Unit,
    onDismiss: () -> Unit,
    onSave: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Save Routine") },
        text = {
            OutlinedTextField(
                value = routineName,
                onValueChange = onNameChange,
                label = { Text("Routine Name") },
                singleLine = true
            )
        },
        confirmButton = {
            Button(
                onClick = onSave,
                enabled = routineName.isNotBlank()
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}