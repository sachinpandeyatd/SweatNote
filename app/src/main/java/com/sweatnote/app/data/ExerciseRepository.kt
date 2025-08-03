package com.sweatnote.app.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class ExerciseRepository(
    private val firestore: FirebaseFirestore,
    private val exerciseDao: ExerciseDao,
    private val anatomyDao: AnatomyDao,
    private val cacheInfoDao: CacheInfoDao
) {
    val exercisesWithMuscles: Flow<List<ExerciseWithMuscles>> = exerciseDao.getAllExercisesWithMuscles()

    suspend fun refreshExerciseIfStale(){
        val cacheKey = "exercise_cache"
        val cacheInfo = cacheInfoDao.get(cacheKey)
        val lastRefreshed = cacheInfo?.lastRefreshed ?: 0
        val cacheAge = System.currentTimeMillis() - lastRefreshed

        if(cacheAge > TimeUnit.HOURS.toMillis(24)){
            Log.d("ExerciseRepository", "Cache is stale. Refreshing from Firestore.")
            try{
                val exercisesSnapshot = firestore.collection("exercises").get().await()
                val musclesSnapshot = firestore.collection("muscles").get().await()

                exerciseDao.deleteAll()
                anatomyDao.deleteAllMuscles()
                anatomyDao.deleteAllPrimaryLinks()
                anatomyDao.deleteAllSecondaryLinks()

                val musclesToCache = musclesSnapshot.map { doc ->
                    Muscle(id = doc.id, name = doc.getString("name") ?: "")
                }
                anatomyDao.insertMuscles(musclesToCache)

                val nameToIdMap = mutableMapOf<String, Int>()

                val exercisesToCache = exercisesSnapshot.map { doc ->
                    Exercise(
                        name = doc.getString("name") ?: "",
                        equipment = (doc.get("equipment") as? List<String>)?.joinToString(", "),
                        notes = doc.getString("notes")
                    )
                }

                val newIds = exerciseDao.insertAllAndGetIds(exercisesToCache) // We'll add this DAO method
                for (i in exercisesToCache.indices) {
                    nameToIdMap[exercisesToCache[i].name] = newIds[i].toInt()
                }

                val primaryLinks = mutableListOf<ExercisePrimaryMuscle>()
                val secondaryLinks = mutableListOf<ExerciseSecondaryMuscle>()

                for (doc in exercisesSnapshot) {
                    val exerciseName = doc.getString("name") ?: ""
                    val roomId = nameToIdMap[exerciseName]
                    if (roomId != null) {
                        (doc.get("primaryMuscles") as? List<String>)?.forEach { muscleId ->
                            primaryLinks.add(ExercisePrimaryMuscle(roomId, muscleId))
                        }
                        (doc.get("secondaryMuscles") as? List<String>)?.forEach { muscleId ->
                            secondaryLinks.add(ExerciseSecondaryMuscle(roomId, muscleId))
                        }
                    }
                }

                anatomyDao.insertPrimaryMuscleLinks(primaryLinks)
                anatomyDao.insertSecondaryMuscleLinks(secondaryLinks)

                cacheInfoDao.upsert(CacheInfo(key = "exercises_cache", lastRefreshed = System.currentTimeMillis()))
                Log.d("ExerciseRepository", "Cache successfully refreshed.")
            }catch (e: Exception){
                Log.e("ExerciseRepository", "Error refreshing cache from Firestore", e)
            }
        }else {
            Log.d("ExerciseRepository", "Cache is fresh. No refresh needed.")
        }
    }
}