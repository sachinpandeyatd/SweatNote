package com.sweatnote.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Transaction
    @Query("SELECT * FROM routines ORDER BY name ASC")
    fun getAllRoutinesWithExercises(): Flow<List<RoutineWithExercises>>

    @Insert
    suspend fun insertRoutine(routine: Routine): Long

    @Insert
    suspend fun insertRoutineExercises(routineExercise: List<RoutineExercise>)
}