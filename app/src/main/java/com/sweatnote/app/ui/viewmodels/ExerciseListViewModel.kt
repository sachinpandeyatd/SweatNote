package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweatnote.app.data.Exercise
import com.sweatnote.app.data.ExerciseDao
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ExerciseListViewModel(private val exerciseDao: ExerciseDao) : ViewModel() {
    val exerciseList : StateFlow<List<Exercise>> = exerciseDao.getAllExercises().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptyList()
    )
}