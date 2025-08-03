package com.sweatnote.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CacheInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(cacheInfo: CacheInfo)

    @Query("SELECT * FROM cache_info WHERE `key` = :key")
    suspend fun get(key: String): CacheInfo?
}