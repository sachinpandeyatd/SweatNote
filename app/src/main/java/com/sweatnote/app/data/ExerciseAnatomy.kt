package com.sweatnote.app.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName = "exercise_primary_muscles",
    primaryKeys = ["exerciseId", "muscleId"]
)
data class ExercisePrimaryMuscle(
    val exerciseId: Int, // Foreign key to Exercise
    val muscleId: String   // Foreign key to Muscle
)

@Entity(
    tableName = "exercise_secondary_muscles",
    primaryKeys = ["exerciseId", "muscleId"]
)
data class ExerciseSecondaryMuscle(
    val exerciseId: Int, // Foreign key to Exercise
    val muscleId: String   // Foreign key to Muscle
)

data class ExerciseWithMuscles(
    @Embedded val exercise: Exercise,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ExercisePrimaryMuscle::class,
            parentColumn = "exerciseId",
            entityColumn = "muscleId"
        )
    )
    val primaryMuscles: List<Muscle>,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = ExerciseSecondaryMuscle::class,
            parentColumn = "exerciseId",
            entityColumn = "muscleId"
        )
    )
    val secondaryMuscles: List<Muscle>
)