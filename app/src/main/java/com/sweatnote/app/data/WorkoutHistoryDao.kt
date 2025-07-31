package com.sweatnote.app.data

import androidx.room.Dao
import androidx.room.Insert

@Dao
interface WorkoutHistoryDao{
    @Insert
    suspend fun insertSession(session: WorkoutSession): Long

    @Insert
    suspend fun insertSessionExercise(sessionExercise: SessionExercise): Long

    @Insert
    suspend fun insertSessionSet(sessionSet: SessionSet)
}
