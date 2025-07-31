package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sweatnote.app.data.ExerciseDao
import com.sweatnote.app.data.LiveWorkoutExercise
import com.sweatnote.app.navigation.LiveWorkoutScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LiveWorkoutViewModel(savedStateHandle: SavedStateHandle, private val exerciseDao: ExerciseDao) : ViewModel(){
    private val _uiState = MutableStateFlow<List<LiveWorkoutExercise>> (emptyList())
    val uiState = _uiState.asStateFlow()

    init{
        val exerciseIdsString: String = checkNotNull(savedStateHandle[LiveWorkoutScreen.exerciseIdsArg])
        fetchExercises(exerciseIdsString)
    }

    private fun fetchExercises(idsString: String){
        viewModelScope.launch{
            val ids = idsString.split(',').mapNotNull { it.toIntOrNull() }
            if(ids.isNotEmpty()){
                val exercisesFromDb = exerciseDao.getExercisesByIds(ids)
                val workoutExercises = exercisesFromDb.map{dbExercise ->
                    LiveWorkoutExercise(exercise = dbExercise)
                }
                _uiState.value = workoutExercises
            }
        }
    }
}