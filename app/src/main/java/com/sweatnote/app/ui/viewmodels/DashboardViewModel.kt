package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweatnote.app.data.WorkoutHistoryDao
import com.sweatnote.app.data.WorkoutSessionWithDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class DashboardViewModel(workoutHistoryDao: WorkoutHistoryDao) : ViewModel() {
    val lastWorkout: StateFlow<WorkoutSessionWithDetails?> =
        workoutHistoryDao.getLatestSessionWithDetails()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = null
            )
}