package com.sweatnote.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscle_groups")
data class MuscleGroup(
    @PrimaryKey val id: String, // e.g., "chest", "back", "shoulders"
    val name: String            // e.g., "Chest", "Back", "Shoulders"
)

@Entity(tableName = "muscles")
data class Muscle(
    @PrimaryKey val id: String,         // e.g., "pec_major_clavicular", "latissimus_dorsi"
    val name: String,                   // e.g., "Upper Chest (Clavicular Head)"
    val muscleGroupId: String? = null   // Foreign key to MuscleGroup (e.g., "chest")
//    val svgPathData: String             // The actual SVG path data for highlighting
)
