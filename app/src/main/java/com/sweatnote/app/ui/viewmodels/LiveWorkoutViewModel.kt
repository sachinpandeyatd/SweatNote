package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sweatnote.app.data.ExerciseDao
import com.sweatnote.app.data.LiveWorkoutExercise
import com.sweatnote.app.data.SessionExercise
import com.sweatnote.app.data.SessionSet
import com.sweatnote.app.data.WorkoutHistoryDao
import com.sweatnote.app.data.WorkoutSession
import com.sweatnote.app.data.WorkoutSet
import com.sweatnote.app.navigation.LiveWorkoutScreen
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class LiveWorkoutViewModel(savedStateHandle: SavedStateHandle, private val exerciseDao: ExerciseDao, private val workoutHistoryDao: WorkoutHistoryDao) : ViewModel(){
    private val _uiState = MutableStateFlow<List<LiveWorkoutExercise>> (emptyList())
    val uiState = _uiState.asStateFlow()

    init{
        val exerciseIdsString: String = checkNotNull(savedStateHandle[LiveWorkoutScreen.exerciseIdsArg])
        fetchExercises(exerciseIdsString)
    }

    private val _navigateBack = MutableSharedFlow<Unit>()
    val navigateBack = _navigateBack.asSharedFlow()

    fun finishWorkout(){
        viewModelScope.launch {
            val newSessionId = workoutHistoryDao.insertSession(WorkoutSession())

            _uiState.value.forEach{liveExercise ->
                val completedSets = liveExercise.sets.filter { it.reps.isNotBlank() }
                if(completedSets.isNotEmpty()){
                    val newSessionExerciseId = workoutHistoryDao.insertSessionExercise(
                        SessionExercise(
                            sessionId = newSessionId,
                            baseExerciseId = liveExercise.exercise.id,
                            exerciseName = liveExercise.exercise.name
                        )
                    )
                    completedSets.forEach{set ->
                        workoutHistoryDao.insertSessionSet(
                            SessionSet(
                                sessionExerciseId = newSessionExerciseId,
                                weight = set.weight.toDoubleOrNull() ?: 0.0,
                                reps = set.reps.toIntOrNull() ?: 0
                            )
                        )
                    }
                }
            }

            _navigateBack.emit(Unit)
        }
    }

    private fun fetchExercises(idsString: String){
        viewModelScope.launch{
            val ids = idsString.split(',').mapNotNull { it.toIntOrNull() }
            if(ids.isNotEmpty()){
                val exercisesFromDb = exerciseDao.getExercisesByIds(ids)
                val workoutExercises = exercisesFromDb.map{dbExercise ->
                    val previousSets = workoutHistoryDao.getLatestSetsForExercise(dbExercise.id)

                    LiveWorkoutExercise(exercise = dbExercise, previousPerformance = previousSets)
                }
                _uiState.value = workoutExercises
            }
        }
    }

    fun onWeightChanged(exerciseId: Int, setId: java.util.UUID, newWeight: String) {
        _uiState.update { currentState ->
            currentState.map { liveExercise ->
                if (liveExercise.exercise.id == exerciseId) {
                    val updatedSets = liveExercise.sets.map { set ->
                        if (set.id == setId) set.copy(weight = newWeight) else set
                    }
                    liveExercise.copy(sets = updatedSets.toMutableList())
                } else {
                    liveExercise
                }
            }
        }
    }

    fun onRepsChanged(exerciseId: Int, setId: java.util.UUID, newReps: String) {
        _uiState.update { currentState ->
            currentState.map { liveExercise ->
                if (liveExercise.exercise.id == exerciseId) {
                    val updatedSets = liveExercise.sets.map { set ->
                        if (set.id == setId) set.copy(reps = newReps) else set
                    }
                    liveExercise.copy(sets = updatedSets.toMutableList())
                } else {
                    liveExercise
                }
            }
        }
    }

    fun addSet(exerciseId: Int) {
        _uiState.update { currentState ->
            currentState.map { liveExercise ->
                if (liveExercise.exercise.id == exerciseId) {
                    val updatedSets = liveExercise.sets.toMutableList().apply {
                        add(WorkoutSet())
                    }
                    liveExercise.copy(sets = updatedSets)
                } else {
                    liveExercise
                }
            }
        }
    }

    fun toggleSetCompletion(exerciseId: Int, setId: UUID) {
        _uiState.update { currentState ->
            currentState.map { liveExercise ->
                if(liveExercise.exercise.id == exerciseId){
                    val updatedSets = liveExercise.sets.map { set ->
                        if (set.id == setId) set.copy(isCompleted = !set.isCompleted) else set
                    }

                    liveExercise.copy(sets = updatedSets.toMutableList())
                }else{
                    liveExercise
                }
            }
        }
    }

    fun deleteSet(exerciseId: Int, setId: UUID) {
        _uiState.update { currentState ->
            currentState.map { liveExercise ->
                if (liveExercise.exercise.id == exerciseId) {
                    val updatedSets = liveExercise.sets.filterNot { it.id == setId }
                    liveExercise.copy(sets = updatedSets.toMutableList())
                } else {
                    liveExercise
                }
            }
        }
    }
}