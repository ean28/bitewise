package com.bitewise.app.feature.ai.api

import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.feature.product.api.Product

enum class LocalSafetyStatus {
    SAFE,
    CRITICAL_DANGER
}

data class ScoredProduct(
    val product: Product,
    val score: Float,
    val analysis: AiAnalysisEntity? = null,
    val safetyStatus: LocalSafetyStatus = LocalSafetyStatus.SAFE,
    val safetyReasoning: String? = null
) {
    val isSafetyRisk: Boolean get() = safetyStatus == LocalSafetyStatus.CRITICAL_DANGER
}

data class LocalSafetyResult(
    val status: LocalSafetyStatus,
    val score: Float,
    val reasoning: String? = null
)
