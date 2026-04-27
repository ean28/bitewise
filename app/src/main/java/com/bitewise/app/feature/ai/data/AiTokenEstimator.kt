package com.bitewise.app.feature.ai.data

import com.bitewise.app.domain.user.models.UserContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

object AiTokenEstimator {
    private const val CHARS_PER_TOKEN = 4
    
    private val json = Json { 
        encodeDefaults = false 
        ignoreUnknownKeys = true
    }

    fun estimateSingleBatchTokens(batchSize: Int, user: UserContext?): Int {
        if (batchSize <= 0) return 0
        
        val systemPromptLen = AiCommunicationSchema.SYSTEM_PROMPT.length
        
        val userPayloadLen = user?.let {
            val payload = AiCommunicationSchema.UserPayload(
                age = it.age,
                weight = it.weight,
                height = it.height,
                activity = it.activity,
                conditions = it.conditions,
                allergies = it.allergies,
                diet = it.dietary.joinToString()
            )
            try {
                json.encodeToString(payload).length
            } catch (e: Exception) {
                250
            }
        } ?: 250

        val avgProductSize = 580
        
        val totalChars = systemPromptLen + userPayloadLen + (batchSize * avgProductSize)
        
        return (totalChars / CHARS_PER_TOKEN).coerceAtLeast(1)
    }
    fun estimateTotalSyncTokens(totalItems: Int, batchSize: Int, user: UserContext?): Int {
        if (totalItems <= 0 || batchSize <= 0) return 0
        val numBatches = (totalItems + batchSize - 1) / batchSize
        return numBatches * estimateSingleBatchTokens(batchSize, user)
    }

    fun estimateDurationSeconds(totalItems: Int, batchSize: Int): Int {
        if (totalItems <= 0 || batchSize <= 0) return 0
        val numBatches = (totalItems + batchSize - 1) / batchSize
        return numBatches * 5
    }
}
