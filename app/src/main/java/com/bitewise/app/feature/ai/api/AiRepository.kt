package com.bitewise.app.feature.ai.api

import com.bitewise.app.feature.ai.data.AiSyncStatus
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.feature.ai.data.local.AiSyncLog
import com.bitewise.app.feature.product.api.Product
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    suspend fun getNextProductsForAi(lastBarcode: String, contextHash: Int, limit: Int): List<Product>
    suspend fun getExistingAnalysis(barcode: String): AiAnalysisEntity?
    
    suspend fun saveAnalysis(
        barcode: String,
        score: Double,
        summary: String,
        tags: List<String>,
        contextHash: Int,
        productHash: Int,
        isLocalOverride: Boolean = false,
        syncStatus: AiSyncStatus = AiSyncStatus.SYNCED,
        tokenCost: Int = 0
    )

    suspend fun saveAnalyses(analyses: List<AiAnalysisEntity>)

    fun getAnalyzedCountFlow(): Flow<Int>
    fun getScannableCountFlow(query: String): Flow<Int>
    fun isCacheStale(currentHash: Int): Flow<Boolean>
    suspend fun forceRefreshCache(currentHash: Int)
    fun triggerSync(limit: Int? = null)

    // Database Inspector & Sync methods
    fun getAllAnalyses(): Flow<List<AiAnalysisEntity>>
    suspend fun clearAllAnalyses()

    // Sync Logs
    fun getRecentSyncLogs(): Flow<List<AiSyncLog>>
    suspend fun addSyncLog(log: AiSyncLog)

    // Stats for Sync UI
    suspend fun getStaleItemCount(currentHash: Int): Int
    suspend fun getFreshItemCount(currentHash: Int): Int
    suspend fun getItemsNeedingSyncCount(currentHash: Int): Int
}
