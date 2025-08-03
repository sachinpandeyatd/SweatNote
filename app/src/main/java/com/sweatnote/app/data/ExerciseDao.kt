package com.sweatnote.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercises(): Flow<List<Exercise>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercise(exercise: Exercise)

    @Query("SELECT * FROM exercises WHERE id IN (:ids)")
    suspend fun getExercisesByIds(ids: List<Int>): List<Exercise>

    @Query("DELETE FROM exercises")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllAndGetIds(exercises: List<Exercise>): List<Long>

    @Transaction
    @Query("SELECT * FROM exercises ORDER BY name ASC")
    fun getAllExercisesWithMuscles(): Flow<List<ExerciseWithMuscles>>
}