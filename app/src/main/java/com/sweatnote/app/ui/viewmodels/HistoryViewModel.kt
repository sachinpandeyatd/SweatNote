package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sweatnote.app.data.WorkoutHistoryDao
import com.sweatnote.app.data.WorkoutSessionWithDetails
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class HistoryViewModel (workoutHistoryDao: WorkoutHistoryDao) : ViewModel() {
    val historyState: StateFlow<List<WorkoutSessionWithDetails>> =
        workoutHistoryDao.getAllSessionsWithDetails()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
}