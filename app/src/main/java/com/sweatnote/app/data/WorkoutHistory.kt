package com.sweatnote.app.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

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

data class SessionExerciseWithSets(
    @Embedded val sessionExercise: SessionExercise,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionExerciseId"
    )
    val sets: List<SessionSet>
)

data class WorkoutSessionWithDetails(
    @Embedded val session: WorkoutSession,
    @Relation(
        entity = SessionExercise::class,
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val exercises: List<SessionExerciseWithSets>
)