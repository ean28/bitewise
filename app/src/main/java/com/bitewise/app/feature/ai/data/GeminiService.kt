package com.bitewise.app.feature.ai.data

import android.util.Log
import com.bitewise.app.BuildConfig
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.BlockThreshold
import com.google.ai.client.generativeai.type.HarmCategory
import com.google.ai.client.generativeai.type.ResponseStoppedException
import com.google.ai.client.generativeai.type.SafetySetting
import com.google.ai.client.generativeai.type.generationConfig
import kotlinx.serialization.json.Json

class GeminiService(private val systemInstruction: String) {

    private val json = Json { ignoreUnknownKeys = true }

    private val config = generationConfig {
        temperature = AiConfiguration.TEMPERATURE
        topK = AiConfiguration.TOP_K
        topP = AiConfiguration.TOP_P
        maxOutputTokens = AiConfiguration.MAX_OUTPUT_TOKENS
        responseMimeType = "application/json"
    }

    private val model = GenerativeModel(
        modelName = AiConfiguration.MODEL_NAME,
        apiKey = BuildConfig.GEMINI_API_KEY,
        generationConfig = config,
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.HATE_SPEECH, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.SEXUALLY_EXPLICIT, BlockThreshold.ONLY_HIGH),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.ONLY_HIGH),
        ),
        systemInstruction = com.google.ai.client.generativeai.type.content {
            text(systemInstruction)
        }
    )

    suspend fun analyzeBatch(payload: String): AiCommunicationSchema.BatchResponse? {
        if (payload.isBlank()) {
            Log.e("GeminiService", "Empty payload sent to analyzeBatch")
            return null
        }
        
        return try {
            val response = model.generateContent(payload)
            Log.d("GEMINI_PAYLOAD", "syst")
            val text = response.text
            if (text == null) {
                Log.w("GeminiService", "Gemini returned a null text response. Prompt Feedback: ${response.promptFeedback}")
                return null
            }
            
            try {
                json.decodeFromString<AiCommunicationSchema.BatchResponse>(text)
            } catch (e: Exception) {
                Log.e("GeminiService", "Failed to decode JSON response. Raw text: $text", e)
                null
            }
        } catch (e: ResponseStoppedException) {
            Log.e("GeminiService", "Response stopped (Safety or Tokens). Feedback: ${e.message}")
            throw e
        } catch (e: Exception) {
            val errorMsg = e.message ?: "Unknown error"
            Log.e("GeminiService", "Error during generateContent. Type: ${e.javaClass.simpleName}. Msg: $errorMsg")
            null
        }
    }

//
//    suspend fun countTokens(payload: String): Int {
//        return try {
//            model.countTokens(payload).totalTokens
//        } catch (e: Exception) {
//            Log.e("GeminiService", "Error counting tokens: ${e.message}")
//            0
//        }
//    }
}
