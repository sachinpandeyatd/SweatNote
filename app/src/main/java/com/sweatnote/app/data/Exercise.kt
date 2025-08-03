package com.sweatnote.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,

    val equipment: String? = null, // e.g., "Barbell", "Dumbbell", "Cable"
    val notes: String? = null      // e.g., "Focus on movements where the arms move upward"
)