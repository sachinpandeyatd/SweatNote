package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sweatnote.app.SweatNoteApplication


object AppViewModelProvider{
    val Factory = viewModelFactory {
        initializer {
            ExerciseListViewModel(
                exerciseRepository = sweatNoteApplication().exerciseRepository
            )
        }

        initializer {
            LiveWorkoutViewModel(
                savedStateHandle = this.createSavedStateHandle(),
                exerciseDao = sweatNoteApplication().database.exerciseDao(),
                workoutHistoryDao = sweatNoteApplication().database.workoutHistoryDao()
            )
        }

        initializer {
            HistoryViewModel(
                workoutHistoryDao = sweatNoteApplication().database.workoutHistoryDao()
            )
        }

        initializer {
            RoutinesViewModel(
                routineDao = sweatNoteApplication().database.routineDao()
            )
        }
    }
}

fun CreationExtras.sweatNoteApplication(): SweatNoteApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SweatNoteApplication)