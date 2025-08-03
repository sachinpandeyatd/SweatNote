package com.sweatnote.app

import android.app.Application
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.sweatnote.app.data.ExerciseRepository
import com.sweatnote.app.data.SweatNoteDatabase

class SweatNoteApplication : Application() {
    val database: SweatNoteDatabase by lazy { SweatNoteDatabase.getDatabase(this) }

    val exerciseRepository: ExerciseRepository by lazy {
        ExerciseRepository(
            firestore = Firebase.firestore,
            exerciseDao = database.exerciseDao(),
            anatomyDao = database.anatomyDao(),
            cacheInfoDao = database.cacheInfoDao()
        )
    }
}