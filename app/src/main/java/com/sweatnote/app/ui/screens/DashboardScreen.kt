package com.sweatnote.app.ui.screens

import com.sweatnote.app.R
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sweatnote.app.data.WorkoutSessionWithDetails
import com.sweatnote.app.navigation.ExerciseListScreen
import com.sweatnote.app.navigation.Screen
import com.sweatnote.app.ui.viewmodels.AppViewModelProvider
import com.sweatnote.app.ui.viewmodels.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(navController: NavController,
   viewModel: DashboardViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val lastWorkout by viewModel.lastWorkout.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text("SweatNote", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(32.dp))

            LastWorkoutCard(session = lastWorkout)

            Spacer(modifier = Modifier.height(32.dp))

            Text("Muscle Recovery", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))

            Box(modifier = Modifier.size(200.dp)){
                Image(
                    painter = painterResource(id = R.drawable.body_outline),
                    contentDescription = "Human Body Outline",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onSurfaceVariant)
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                onClick = { navController.navigate(com.sweatnote.app.navigation.ExerciseListScreen.routeForWorkout) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start Empty Workout")
            }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { navController.navigate(com.sweatnote.app.navigation.Screen.Routines.route) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Start from Routine")
            }
        }
    }
}

@Composable
fun LastWorkoutCard(session: WorkoutSessionWithDetails?) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ){
        Column(modifier = Modifier.padding(16.dp)){
            Text(
                "Last Workout",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            if(session == null){
                Text("No workouts logged yet. Let's get started.", style = MaterialTheme.typography.bodyMedium)
            }else{
                val dateFormatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
                val formattedDate = dateFormatter.format(Date(session.session.date))
                Text(formattedDate, style = MaterialTheme.typography.bodyLarge)

                val exerciseCount = session.exercises.size
                Text(
                    "$exerciseCount exercises",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}