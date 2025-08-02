package com.sweatnote.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sweatnote.app.data.WorkoutHistoryDao
import com.sweatnote.app.data.WorkoutSessionWithDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId

class HistoryViewModel (workoutHistoryDao: WorkoutHistoryDao) : ViewModel() {
    private val allSession = workoutHistoryDao.getAllSessionsWithDetails()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate.asStateFlow()

    private val _visibleMonth = MutableStateFlow(YearMonth.now())
    val visibleMonth: StateFlow<YearMonth> = _visibleMonth.asStateFlow()

    val workoutDates: StateFlow<Set<LocalDate>> = allSession.map { sessions ->
        sessions.map {
            java.time.Instant.ofEpochMilli(it.session.date)
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate()
        }.toSet()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = emptySet()
    )

    val sessionForSelectedDate: StateFlow<List<WorkoutSessionWithDetails>> =
        combine(allSession, _selectedDate){sessions, date ->
            sessions.filter {
                val sessionDate = Instant.ofEpochMilli(it.session.date)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()
                sessionDate == date
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    fun onDateSelected(date: LocalDate){
        _selectedDate.value = date
    }

    fun goToPreviousMonth() {
        _visibleMonth.value = _visibleMonth.value.minusMonths(1)
    }

    fun goToNextMonth() {
        _visibleMonth.value = _visibleMonth.value.plusMonths(1)
    }
}