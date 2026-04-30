package com.bitewise.app.feature.ai.domain

import com.bitewise.app.feature.ai.AiPayloadProjector
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.api.LocalSafetyResult
import com.bitewise.app.feature.ai.api.LocalSafetyStatus
import com.bitewise.app.feature.ai.api.ScoredProduct
import com.bitewise.app.feature.ai.data.AiConfiguration
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class HealthScoringEngine(
    private val productRepo: ProductRepository,
    private val userRepo: UserRepository,
    private val aiRepo: AiRepository,
    private val projector: AiPayloadProjector
) {

    fun observeRecommendations(): Flow<List<ScoredProduct>> {
        return combine(
            userRepo.getUserContext(),
            aiRepo.getAllAnalyses(),
            productRepo.getScannableProductsFlow()
        ) { user, analyses, allProducts ->
            if (user == null) return@combine emptyList()

            allProducts.map { product ->
                val analysis = analyses.find { it.barcode == product.code }
                val safety = calculateLocalSafety(product, user)

                val score = if (safety.status == LocalSafetyStatus.CRITICAL_DANGER) {
                    0f
                } else {
                    analysis?.score?.toFloat() ?: calculateLocalHeuristicScore(product, user)
                }

                ScoredProduct(
                    product = product,
                    score = score,
                    analysis = analysis,
                    safetyStatus = safety.status,
                    safetyReasoning = safety.reasoning
                )
            }.sortedByDescending { it.score }
        }
    }

    fun calculateLocalSafety(product: Product, user: UserContext): LocalSafetyResult {
        if (user.allergies.isEmpty()) return LocalSafetyResult(LocalSafetyStatus.SAFE, 100f)

        val ingredients = product.ingredientsText?.lowercase().orEmpty()
        
        val detected = user.allergies
            .map { it.trim().lowercase() }
            .filter { it.isNotBlank() }
            .filter { query ->
                Regex("\\b${Regex.escape(query)}\\b").containsMatchIn(ingredients)
            }

        return if (detected.isNotEmpty()) {
            LocalSafetyResult(
                status = LocalSafetyStatus.CRITICAL_DANGER,
                score = 0f,
                reasoning = "Matches allergies: ${detected.joinToString(", ")}"
            )
        } else LocalSafetyResult(LocalSafetyStatus.SAFE, 100f)
    }

    fun calculateLocalHeuristicScore(product: Product, user: UserContext): Float {
        var score = AiConfiguration.Heuristics.BASE_SCORE
        
        score += product.productScores?.nutritionGrade?.let {
            when (it.lowercase()) {
                "a" -> AiConfiguration.Heuristics.NUTRI_A_BONUS
                "b" -> AiConfiguration.Heuristics.NUTRI_B_BONUS
                "d" -> AiConfiguration.Heuristics.NUTRI_D_PENALTY
                "e" -> AiConfiguration.Heuristics.NUTRI_E_PENALTY
                else -> 0f
            }
        } ?: 0f

        score += product.productScores?.novaGroup?.let {
            when (it) { 
                1 -> AiConfiguration.Heuristics.NOVA_1_BONUS
                2 -> AiConfiguration.Heuristics.NOVA_2_BONUS
                3 -> AiConfiguration.Heuristics.NOVA_3_PENALTY
                4 -> AiConfiguration.Heuristics.NOVA_4_PENALTY
                else -> 0f 
            }
        } ?: 0f

        val isSugarSensitive = user.dietary.any { it.contains("sugar", true) || it.contains("keto", true) }
        if (isSugarSensitive) {
            val sugar = product.productNutriments?.find { it.name.contains("suga", true) }?.amount ?: 0f
            if (sugar > AiConfiguration.Heuristics.HIGH_SUGAR_THRESHOLD) {
                score += AiConfiguration.Heuristics.DIETARY_PENALTY
            }
        }
        return score.coerceIn(0f, 100f)
    }

    fun prepareAiPayload(products: List<Product>, user: UserContext) = 
        projector.createBatchPayload(products, user)
}
