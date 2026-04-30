package com.bitewise.app.feature.ai.data

import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AiAnalysisDAO {
    @Query("SELECT barcode FROM product_ai_analysis")
    suspend fun getAnalyzedBarcodes(): List<String>

    @Query("SELECT barcode FROM product_ai_analysis WHERE user_context_hash = :contextHash")
    suspend fun getAnalyzedBarcodesForContext(contextHash: Int): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalysis(analysis: AiAnalysisEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnalyses(analyses: List<AiAnalysisEntity>)

    @Query("SELECT * FROM product_ai_analysis WHERE barcode = :barcode")
    suspend fun getAnalysisForProduct(barcode: String): AiAnalysisEntity?

    @Query("SELECT COUNT(*) FROM product_ai_analysis")
    fun getAnalyzedCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM product_ai_analysis WHERE user_context_hash != :currentHash")
    suspend fun getCountWithDifferentHash(currentHash: Int): Int

    @Query("DELETE FROM product_ai_analysis WHERE user_context_hash != :currentHash")
    suspend fun deleteStaleAnalysis(currentHash: Int)

    @Query("DELETE FROM product_ai_analysis")
    suspend fun clearAllAnalysis()

    @Query("SELECT * FROM product_ai_analysis ORDER BY last_analyzed DESC")
    fun getAllAnalyses(): Flow<List<AiAnalysisEntity>>
}
