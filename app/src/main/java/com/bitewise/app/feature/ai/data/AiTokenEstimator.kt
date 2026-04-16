package com.bitewise.app.feature.ai.data

object AiTokenEstimator {
    private const val CHARS_PER_TOKEN = 4

    fun estimate(text: String): Int {
        if (text.isEmpty()) return 0
        return (text.length / CHARS_PER_TOKEN).coerceAtLeast(1)
    }
}
