package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.sweatnote.app.data.ExerciseDao
import com.sweatnote.app.navigation.LiveWorkoutScreen

class LiveWorkoutViewModel(savedStateHandle: SavedStateHandle, exerciseDao: ExerciseDao) : ViewModel(){
    val exerciseIds: String = checkNotNull(savedStateHandle[LiveWorkoutScreen.exerciseIdsArg])
}