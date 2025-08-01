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
}
