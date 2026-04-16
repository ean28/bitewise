package com.bitewise.app.feature.ai.data.local
import com.bitewise.app.feature.ai.data.AiSyncStatus


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_ai_analysis")
data class AiAnalysisEntity(
    @PrimaryKey val barcode: String,
    val score: Double,
    val summary: String,
    val dynamicTags: String,
    val userContextHash: Int,
    val productHash: Int,
    val isLocalOverride: Boolean = false,
    val lastAnalyzed: Long = System.currentTimeMillis(),
    val syncStatus: AiSyncStatus = AiSyncStatus.PENDING,
    val tokenCost: Int = 0
)
