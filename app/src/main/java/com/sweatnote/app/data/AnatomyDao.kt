package com.sweatnote.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnatomyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuscleGroups(groups: List<MuscleGroup>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuscles(muscles: List<Muscle>)

    // We also need to insert exercises and their links here now
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<Exercise>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPrimaryMuscleLinks(links: List<ExercisePrimaryMuscle>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSecondaryMuscleLinks(links: List<ExerciseSecondaryMuscle>)

    @Query("DELETE FROM muscles")
    suspend fun deleteAllMuscles()
    @Query("DELETE FROM exercise_primary_muscles")
    suspend fun deleteAllPrimaryLinks()
    @Query("DELETE FROM exercise_secondary_muscles")
    suspend fun deleteAllSecondaryLinks()

    // The key query for your UI
    @Query("""
        SELECT * FROM muscles WHERE id IN (
            SELECT muscleId FROM exercise_primary_muscles WHERE exerciseId = :exerciseId
            UNION
            SELECT muscleId FROM exercise_secondary_muscles WHERE exerciseId = :exerciseId
        )
    """)
    suspend fun getMusclesForExercise(exerciseId: Int): List<Muscle>
}