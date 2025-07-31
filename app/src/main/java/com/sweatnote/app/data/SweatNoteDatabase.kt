package com.sweatnote.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Exercise::class], version = 1, exportSchema = false)
abstract class SweatNoteDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao

    companion object{
        @Volatile
        private var Instance: SweatNoteDatabase? = null

        fun getDatabase(context: Context): SweatNoteDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, SweatNoteDatabase::class.java, "sweatnote_database")
                    .fallbackToDestructiveMigration().build().also { Instance = it }
            }
        }
    }
}