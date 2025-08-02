package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweatnote.app.data.Routine
import com.sweatnote.app.data.RoutineDao
import com.sweatnote.app.data.RoutineExercise
import com.sweatnote.app.data.RoutineWithExercises
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RoutinesViewModel(private val routineDao: RoutineDao) : ViewModel() {
    val routines: StateFlow<List<RoutineWithExercises>> =
        routineDao.getAllRoutinesWithExercises()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )

    fun saveRoutine(routineName: String, exerciseIds: List<Int>){
        viewModelScope.launch {
            val newRoutineId = routineDao.insertRoutine(Routine(name = routineName))
            val routineExercises = exerciseIds.mapIndexed{index, exerciseId ->
                RoutineExercise(
                    routineId = newRoutineId,
                    exerciseId = exerciseId,
                    exerciseOrder = index
                )
            }

            routineDao.insertRoutineExercises(routineExercises)
        }
    }
}