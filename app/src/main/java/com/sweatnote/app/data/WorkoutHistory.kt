package com.sweatnote.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_exercises")
data class SessionExercise(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionId: Long,
    val baseExerciseId: Int,
    val exerciseName: String
)

@Entity(tableName = "session_sets")
data class SessionSet(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val sessionExerciseId: Long,
    val weight: Double,
    val reps: Int
)