package com.bitewise.app.feature.ai.data

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.edit
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.bitewise.app.feature.ai.data.local.AiSyncLog
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.domain.HealthScoringEngine
import com.bitewise.app.domain.user.repository.UserRepository
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull

class AiBatchWorker(
    context: Context,
    params: WorkerParameters,
    private val aiRepository: AiRepository,
    private val userRepository: UserRepository,
    private val geminiService: GeminiService,
    private val healthScoringEngine: HealthScoringEngine
) : CoroutineWorker(context, params) {

    companion object {
        private const val TAG = "AI_BatchWorker"
        const val KEY_MAX_ITEMS = "max_items"
    }

    override suspend fun doWork(): Result {
        if (!isNetworkAvailable()) {
            Log.w(TAG, "Sync aborted: No internet connection.")
            return Result.failure()
        }

        val prefs = applicationContext.getSharedPreferences(AiConfiguration.PREFS_NAME, Context.MODE_PRIVATE)
        
        val targetBatchSize = prefs.getInt(AiConfiguration.KEY_BATCH_SIZE, AiConfiguration.DEFAULT_BATCH_SIZE)
            .coerceIn(10, AiConfiguration.MAX_BATCH_SIZE)
        
        var batchSize = targetBatchSize

        val user = userRepository.getUserContext().firstOrNull() ?: return Result.failure()
        val userHash = user.hashCode()

        var lastBarcode = prefs.getString(AiConfiguration.KEY_LAST_BARCODE, "") ?: ""
        var totalSuccess = 0
        var totalSkipped = 0
        var totalTokensUsed = 0
        
        val maxPerRun = inputData.getInt(KEY_MAX_ITEMS, 100) 

        try {
            while ((totalSuccess + totalSkipped) < maxPerRun) {
                if (isStopped) {
                    Log.i(TAG, "Worker stopped. Progress checkpoint: $lastBarcode")
                    return Result.failure()
                }

                val remaining = maxPerRun - (totalSuccess + totalSkipped)
                val currentBatchSize = if (remaining < batchSize) remaining else batchSize

                // Fetch products for the current batch
                val products = aiRepository.getNextProductsForAi(lastBarcode, userHash, currentBatchSize)
                if (products.isEmpty()) {
                    prefs.edit { remove(AiConfiguration.KEY_LAST_BARCODE) }
                    break
                }

                //TODO(refactor): choose between chunked or LIMIT in repository
                val chunks = products.chunked(currentBatchSize)
                
                for (chunk in chunks) {
                    if (isStopped) return Result.failure()

                    val batchToSave = mutableListOf<AiAnalysisEntity>()
                    val payload = healthScoringEngine.prepareAiPayload(chunk, user)
                    
                    try {
                        val requestTokens = AiTokenEstimator.estimateSingleBatchTokens(chunk.size, user)
                        val response = geminiService.analyzeBatch(payload)

                        if (response != null && response.results.isNotEmpty()) {
                            totalTokensUsed += requestTokens
                            
                            response.results.forEach { (barcode, analysis) ->
                                val product = chunk.find { it.code == barcode } ?: return@forEach
                                batchToSave.add(AiAnalysisEntity(
                                    barcode = barcode,
                                    score = analysis.score,
                                    summary = analysis.summary,
                                    dynamicTags = analysis.tags.joinToString(", "),
                                    userContextHash = userHash,
                                    productHash = product.hashCode(),
                                    isLocalOverride = false,
                                    syncStatus = AiSyncStatus.SYNCED,
                                    tokenCost = requestTokens / chunk.size
                                ))
                            }
                            // Dynamic Batch Scaling: Grow back towards the user's target if successful
                        } else {
                            // If API fails, handle reduction
                            if (batchSize <= 10) {
                                totalSkipped += chunk.size
                            } else {
                                batchSize = (batchSize / 2).coerceAtLeast(10)
                                prefs.edit { putInt(AiConfiguration.KEY_BATCH_SIZE, batchSize) }
                                return Result.failure()
                            }
                        }
                        delay(AiConfiguration.REQUEST_DELAY_MS) 
                    } catch (e: Exception) {
                        Log.e(TAG, "Batch request failed: ${e.message}")
                        return Result.failure()
                    }

                    if (batchToSave.isNotEmpty()) {
                        aiRepository.saveAnalyses(batchToSave)
                        totalSuccess += batchToSave.size
                    }
                    
                    totalSkipped += (chunk.size - (batchToSave.size))
                    lastBarcode = chunk.last().code
                    prefs.edit { putString(AiConfiguration.KEY_LAST_BARCODE, lastBarcode) }
                    
                    updateProgress(totalSuccess, totalSkipped, maxPerRun)
                }
            }

            prefs.edit { remove(AiConfiguration.KEY_LAST_BARCODE) }
            
            aiRepository.addSyncLog(AiSyncLog(
                totalTokens = totalTokensUsed, 
                itemCount = totalSuccess, 
                skippedCount = totalSkipped,
                status = if (totalSkipped > 0) "PARTIAL" else "SUCCESS"
            ))
            
            return Result.success()
        } catch (e: Exception) {
            Log.e(TAG, "Critical error in sync loop", e)
            aiRepository.addSyncLog(AiSyncLog(
                totalTokens = totalTokensUsed, 
                itemCount = totalSuccess, 
                skippedCount = totalSkipped,
                status = "FAILED"
            ))
            return Result.failure()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val cm = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities = cm.getNetworkCapabilities(cm.activeNetwork)
        return capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
    }

    private suspend fun updateProgress(success: Int, skipped: Int, total: Int) {
        setProgress(workDataOf("progress" to (success + skipped), "total" to total))
    }
}
