package com.sweatnote.app.data

import java.util.UUID

data class WorkoutSet(
    val id: UUID = UUID.randomUUID(),
    var weight: String = "",
    var reps: String = "",
    var isCompleted: Boolean = false
)

data class LiveWorkoutExercise(
    val exercise: Exercise,
    val sets: MutableList<WorkoutSet> = mutableListOf(WorkoutSet()),
    val previousPerformance: List<SessionSet> = emptyList()
)
