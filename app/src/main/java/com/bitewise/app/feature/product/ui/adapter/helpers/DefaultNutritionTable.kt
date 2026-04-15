package com.bitewise.app.feature.product.ui.adapter.helpers

import com.bitewise.app.feature.product.api.Nutrient

object DefaultNutritionTable {

    fun createEmptyState(): List<Nutrient> {
        return listOf(
            Nutrient("Energy", 0f, "unknown"),
            Nutrient("Fat", 0f, "unknown"),
            Nutrient("Saturated fat", 0f, "unknown"),
            Nutrient("Carbohydrates", 0f, "unknown"),
            Nutrient("Sugars", 0f, "unknown"),
            Nutrient("Fiber", 0f, "unknown"),
            Nutrient("Proteins", 0f, "unknown"),
            Nutrient("Salt", 0f, "unknown")
        )
    }
}
