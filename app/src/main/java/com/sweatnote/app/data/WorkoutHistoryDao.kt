package com.sweatnote.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutHistoryDao{
    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    @Insert
    suspend fun insertSessionExercise(sessionExercise: SessionExercise): Long

    @Insert
    suspend fun insertSessionSet(sessionSet: SessionSet)

    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY date DESC")
    fun getAllSessionsWithDetails(): Flow<List<WorkoutSessionWithDetails>>

    @Transaction
    @Query("SELECT * FROM session_sets WHERE sessionExerciseId = (SELECT id FROM session_exercises WHERE baseExerciseId = :baseExerciseId ORDER BY id DESC LIMIT 1)")
    suspend fun getLatestSetsForExercise(baseExerciseId: Int): List<SessionSet>

    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY date DESC LIMIT 1")
    fun getLatestSessionWithDetails(): Flow<WorkoutSessionWithDetails>

    @Transaction
    suspend fun deleteSession(session: WorkoutSession) {
        val sessionExercises = getExercisesForSession(session.id)
        val sessionExerciseIds = sessionExercises.map { it.id }

        deleteSetsBySessionExerciseIds(sessionExerciseIds)
        deleteExercisesBySessionId(session.id)
        deleteSessionById(session.id)
    }

    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun getExercisesForSession(sessionId: Long): List<SessionExercise>

    @Query("DELETE FROM session_sets WHERE sessionExerciseId IN (:sessionExerciseIds)")
    suspend fun deleteSetsBySessionExerciseIds(sessionExerciseIds: List<Long>)

    @Query("DELETE FROM session_exercises WHERE sessionId = :sessionId")
    suspend fun deleteExercisesBySessionId(sessionId: Long)

    @Query("DELETE FROM workout_sessions WHERE id = :sessionId")
    suspend fun deleteSessionById(sessionId: Long)
}
