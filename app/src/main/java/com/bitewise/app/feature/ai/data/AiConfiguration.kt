package com.bitewise.app.feature.ai.data

object AiConfiguration {
    const val MODEL_NAME = "gemini-2.5-flash-lite"
    const val MAX_OUTPUT_TOKENS = 65536
    const val TEMPERATURE = 0.1f
    const val TOP_K = 32
    const val TOP_P = 1f

    const val DEFAULT_BATCH_SIZE = 80
    const val MAX_BATCH_SIZE = 400
    const val MIN_BATCH_SIZE = 80

    // Timing guardrail for 10 RPM limit
    const val REQUEST_DELAY_MS = 1500L

    const val PREFS_NAME = "ai_settings"
    const val KEY_BATCH_SIZE = "ai_batch_size"
    const val KEY_LAST_BARCODE = "ai_last_barcode"
//    const val KEY_RETRY_DELAY = "ai_retry_delay"

    // Heuristic Scoring Constants
    object Heuristics {
        const val BASE_SCORE = 60f
        const val NUTRI_A_BONUS = 30f
        const val NUTRI_B_BONUS = 15f
        const val NUTRI_D_PENALTY = -15f
        const val NUTRI_E_PENALTY = -30f
        
        const val NOVA_1_BONUS = 10f
        const val NOVA_2_BONUS = 5f
        const val NOVA_3_PENALTY = -5f
        const val NOVA_4_PENALTY = -20f

        const val HIGH_SUGAR_THRESHOLD = 15f
        const val DIETARY_PENALTY = -15f
    }
}
