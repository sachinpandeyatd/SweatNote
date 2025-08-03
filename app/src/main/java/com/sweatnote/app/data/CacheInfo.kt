package com.sweatnote.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cache_info")
data class CacheInfo(
    @PrimaryKey val key: String,
    val lastRefreshed: Long
)
