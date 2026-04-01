package com.bitewise.app.ui.product.adapter.helpers

import com.bitewise.app.domain.models.Nutrients

object DefaultNutritionTable {

    fun createEmptyState(): List<Nutrients> {
        return listOf(
            Nutrients("Energy", 0f, "unknown"),
            Nutrients("Fat", 0f, "unknown"),
            Nutrients("Saturated fat", 0f, "unknown"),
            Nutrients("Carbohydrates", 0f, "unknown"),
            Nutrients("Sugars", 0f, "unknown"),
            Nutrients("Fiber", 0f, "unknown"),
            Nutrients("Proteins", 0f, "unknown"),
            Nutrients("Salt", 0f, "unknown")
        )
    }
}