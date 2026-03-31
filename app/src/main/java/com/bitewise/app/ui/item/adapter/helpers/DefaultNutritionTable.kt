package com.bitewise.app.ui.item.adapter.helpers

import com.bitewise.app.domain.Nutrients

object DefaultNutritionTable {

    fun createEmptyState(): List<Nutrients> {
        return listOf(
            Nutrients(name = "Energy", amount = 0f, unit = "kJ <br> <i>(--- kcal)</i>"),
            Nutrients(name = "Fat", amount = 0f, unit = "unknown"),
            Nutrients(name = "Saturated Fat", amount = 0f, unit = "unknown"),
            Nutrients(name = "Carbohydrates", amount = 0f, unit = "unknown"),
            Nutrients(name = "Sugars", amount = 0f, unit = "unknown"),
            Nutrients(name = "Proteins", amount = 0f, unit = "unknown"),
            Nutrients(name = "Salt", amount = 0f, unit = "unknown")
        )
    }
}