package com.sweatnote.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sweatnote.app.data.WorkoutSessionWithDetails
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.HistoryViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel = viewModel(factory = AppViewModelProvider.Factory)){
    val historyState by viewModel.historyState.collectAsState()

    Scaffold (
        topBar = { TopAppBar(title = { Text("Workout History") }) }
    ){innerPadding ->
        LazyColumn (modifier = Modifier.padding(innerPadding)){
            if(historyState.isEmpty()){
                item {
                    Text(
                        text = "No workouts logged yet. Finish a workout to see it here!",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }else{
                items(historyState){session ->
                    WorkoutHistoryCard(session = session)
                }
            }
        }
    }
}

@Composable
fun WorkoutHistoryCard(session: WorkoutSessionWithDetails){
    val dateFormatter = SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault())
    val formattedDate = dateFormatter.format(Date(session.session.date))

    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ){
        Column (modifier = Modifier.padding(16.dp)){
            Text(
                text = formattedDate,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            session.exercises.forEach{exerciseWithSet ->
                Text(
                    text = exerciseWithSet.sessionExercise.exerciseName,
                    style = MaterialTheme.typography.titleMedium
                )

                exerciseWithSet.sets.forEach{set ->
                    Text(
                        text = "  •  ${set.weight} kg x ${set.reps} reps",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}