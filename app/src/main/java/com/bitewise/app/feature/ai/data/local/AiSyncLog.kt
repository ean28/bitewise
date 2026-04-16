package com.bitewise.app.feature.ai.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ai_sync_logs")
data class AiSyncLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val totalTokens: Int,
    val itemCount: Int,
    val skippedCount: Int = 0,
    val status: String // SUCCESS, FAILED, PARTIAL
)
