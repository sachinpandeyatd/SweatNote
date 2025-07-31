package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweatnote.app.data.Exercise
import com.sweatnote.app.data.ExerciseDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ExerciseListViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {
    val exerciseList : StateFlow<List<Exercise>> = exerciseDao.getAllExercises().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )

    private val _selectedExerciseIds = MutableStateFlow<Set<Int>>(emptySet())
    val selectedExerciseIds: StateFlow<Set<Int>> = _selectedExerciseIds.asStateFlow()

    fun toggleExerciseSelection(exerciseId: Int) {
        _selectedExerciseIds.update { currentIds ->
            if (exerciseId in currentIds) {
                currentIds - exerciseId
            } else {
                currentIds + exerciseId
            }
        }
    }
}