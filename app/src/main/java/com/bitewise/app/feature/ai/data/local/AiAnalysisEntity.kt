package com.bitewise.app.feature.ai.data.local
import androidx.room.ColumnInfo
import com.bitewise.app.feature.ai.data.AiSyncStatus


import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_ai_analysis")
data class AiAnalysisEntity(
    @PrimaryKey
    @ColumnInfo(name = "barcode")
    val barcode: String,

    @ColumnInfo(name = "score")
    val score: Double,

    @ColumnInfo(name = "summary")
    val summary: String,

    @ColumnInfo(name = "dynamic_tags")
    val dynamicTags: String,

    @ColumnInfo(name = "user_context_hash")
    val userContextHash: Int,

    @ColumnInfo(name = "product_hash")
    val productHash: Int,

    @ColumnInfo(name = "is_local_override")
    val isLocalOverride: Boolean = false,

    @ColumnInfo(name = "last_analyzed")
    val lastAnalyzed: Long = System.currentTimeMillis(),

    @ColumnInfo(name = "sync_status")
    val syncStatus: AiSyncStatus = AiSyncStatus.PENDING,

    @ColumnInfo(name = "token_cost")
    val tokenCost: Int = 0
)
