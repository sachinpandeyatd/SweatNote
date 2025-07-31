package com.sweatnote.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val primaryMuscle: String,
    val equipment: String,
    val instructions: String? = null
)
