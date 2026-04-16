package com.bitewise.app.feature.product.data

import com.bitewise.app.feature.product.api.Ingredient


object IngredientsProcessor {

    private val riskyKeywords = listOf(
        "sugar", "msg", "tbhq", "artificial", "sweetener"
    )

    fun process(raw: String?): List<Ingredient> {
        if (raw.isNullOrBlank()) return emptyList()

        val regex = Regex("""([^,()]+)(\(([^)]+)\))?""")

        return regex.findAll(raw).map { match ->
            val main = match.groupValues[1].trim()
            val sub = match.groupValues[3]

            val subList = if (sub.isNotEmpty()) {
                sub.split(",").map { it.trim() }
            } else emptyList()

            val cleanedMain = main.lowercase()
                .replaceFirstChar { it.uppercase() }

            val cleanedSub = subList.map {
                it.lowercase().replaceFirstChar { c -> c.uppercase() }
            }

            val isRiskyMain = riskyKeywords.any { cleanedMain.contains(it, true) }
            val isRiskySub = cleanedSub.any { subItem ->
                riskyKeywords.any { subItem.contains(it, true) }
            }

            Ingredient(
                name = cleanedMain,
                subIngredients = cleanedSub,
                isRisky = isRiskyMain || isRiskySub
            )
        }.toList()
    }
}