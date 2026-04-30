package com.bitewise.app.feature.ai.data

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.feature.ai.data.local.AiSyncLog
import com.bitewise.app.feature.product.data.ProductDAO
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.product.data.toDomain
import com.bitewise.app.feature.product.api.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalAiRepository(
    context: Context,
    private val productDao: ProductDAO,
    private val analysisDao: AiAnalysisDAO,
    private val syncLogDao: AiSyncLogDAO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : AiRepository {
    
    private val applicationContext = context.applicationContext

    override suspend fun getNextProductsForAi(lastBarcode: String, contextHash: Int, limit: Int): List<Product> {
        return withContext(defaultDispatcher) {
            val analyzedBarcodes = analysisDao.getAnalyzedBarcodesForContext(contextHash).toSet()
            
            var currentLastBarcode = lastBarcode
            val resultProducts = mutableListOf<Product>()
            
            while (resultProducts.size < limit) {
                val batch = productDao.getNextScannableProducts(currentLastBarcode, limit)
                if (batch.isEmpty()) break
                
                val filtered = batch.filter { it.code !in analyzedBarcodes }
                resultProducts.addAll(filtered.map { it.toDomain() })
                
                currentLastBarcode = batch.last().code
                if (resultProducts.size >= limit) break
            }
            
            resultProducts.take(limit)
        }
    }

    override suspend fun saveAnalysis(
        barcode: String,
        score: Double,
        summary: String,
        tags: List<String>,
        contextHash: Int,
        productHash: Int,
        isLocalOverride: Boolean,
        syncStatus: AiSyncStatus,
        tokenCost: Int
    ){
        withContext(defaultDispatcher) {
            val entity = AiAnalysisEntity(
                barcode = barcode,
                score = score,
                summary = summary,
                dynamicTags = tags.joinToString(", "),
                userContextHash = contextHash,
                productHash = productHash,
                isLocalOverride = isLocalOverride,
                syncStatus = syncStatus,
                tokenCost = tokenCost
            )
            analysisDao.insertAnalysis(entity)
        }
    }

    override suspend fun saveAnalyses(analyses: List<AiAnalysisEntity>) {
        withContext(defaultDispatcher) {
            analysisDao.insertAnalyses(analyses)
        }
    }

    override suspend fun getExistingAnalysis(barcode: String): AiAnalysisEntity? {
        return withContext(defaultDispatcher) {
            analysisDao.getAnalysisForProduct(barcode)
        }
    }

    override fun getAnalyzedCountFlow(): Flow<Int> {
        return analysisDao.getAnalyzedCount()
    }

    override fun getScannableCountFlow(query: String): Flow<Int> {
        return productDao.getScannableCountFlow(query)
    }

    override fun isCacheStale(currentHash: Int): Flow<Boolean> {
        return analysisDao.getAnalyzedCount().map {
            val countWithDifferentHash = analysisDao.getCountWithDifferentHash(currentHash)
            countWithDifferentHash > 0
        }
    }

    override suspend fun forceRefreshCache(currentHash: Int) {
        withContext(defaultDispatcher) {
            analysisDao.deleteStaleAnalysis(currentHash)
            triggerSync()
        }
    }

    override fun triggerSync(limit: Int?) {
        val inputData = if (limit != null) {
            Data.Builder().putInt(AiBatchWorker.KEY_MAX_ITEMS, limit).build()
        } else {
            Data.EMPTY
        }

        val workRequest = OneTimeWorkRequestBuilder<AiBatchWorker>()
            .addTag("AiSync")
            .setInputData(inputData)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniqueWork(
            "AiBatchSync",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
    }

    override fun getAllAnalyses(): Flow<List<AiAnalysisEntity>> {
        return analysisDao.getAllAnalyses()
    }

    override suspend fun clearAllAnalyses() {
        withContext(defaultDispatcher) {
            analysisDao.clearAllAnalysis()
        }
    }

    override fun getRecentSyncLogs(): Flow<List<AiSyncLog>> {
        return syncLogDao.getRecentLogs()
    }

    override suspend fun addSyncLog(log: AiSyncLog) {
        withContext(defaultDispatcher) {
            syncLogDao.insertLog(log)
        }
    }

    //TODO("NOT WORKING")
    override suspend fun getStaleItemCount(currentHash: Int): Int {
        return withContext(defaultDispatcher) {
            analysisDao.getCountWithDifferentHash(currentHash)
        }
    }

    override suspend fun getFreshItemCount(currentHash: Int): Int {
        return withContext(defaultDispatcher) {
            analysisDao.getAnalyzedBarcodesForContext(currentHash).size
        }
    }

    //TODO("Implement")
    override suspend fun getItemsNeedingSyncCount(currentHash: Int): Int {
        return withContext(defaultDispatcher) {
            val totalScannable = productDao.getTotalAiScannableCount()
            val alreadyAnalyzed = analysisDao.getAnalyzedBarcodesForContext(currentHash).size
            (totalScannable - alreadyAnalyzed).coerceAtLeast(0)
        }
    }

}
