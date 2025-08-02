package com.sweatnote.app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Exercise::class,
        WorkoutSession::class,
        SessionExercise::class,
        SessionSet::class,
        Routine::class,
        RoutineExercise::class
    ],
    version = 3,
    exportSchema = false)
abstract class SweatNoteDatabase : RoomDatabase() {
    abstract fun exerciseDao(): ExerciseDao
    abstract fun workoutHistoryDao(): WorkoutHistoryDao
    abstract fun routineDao(): RoutineDao

    companion object{
        @Volatile
        private var Instance: SweatNoteDatabase? = null

        fun getDatabase(context: Context): SweatNoteDatabase{
            return Instance ?: synchronized(this){
                Room.databaseBuilder(context, SweatNoteDatabase::class.java, "sweatnote_database")
                    .fallbackToDestructiveMigration().addCallback(DatabaseCallback(context)).build().also { Instance = it }
            }
        }
    }

    private class DatabaseCallback(private val context: Context) : RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            CoroutineScope(Dispatchers.IO).launch {
                populateDatabase(Instance!!.exerciseDao())
            }
        }

        suspend fun populateDatabase(exerciseDao: ExerciseDao){
            exerciseDao.insertExercise(Exercise(name = "Barbell Bench Press", primaryMuscle = "Chest", equipment = "Barbell"))
            exerciseDao.insertExercise(Exercise(name = "Squat", primaryMuscle = "Legs", equipment = "Barbell"))
            exerciseDao.insertExercise(Exercise(name = "Deadlift", primaryMuscle = "Back", equipment = "Barbell"))
            exerciseDao.insertExercise(Exercise(name = "Overhead Press", primaryMuscle = "Shoulders", equipment = "Barbell"))
            exerciseDao.insertExercise(Exercise(name = "Pull Up", primaryMuscle = "Back", equipment = "Bodyweight"))
            exerciseDao.insertExercise(Exercise(name = "Dumbbell Curl", primaryMuscle = "Biceps", equipment = "Dumbbell"))
        }
    }
}