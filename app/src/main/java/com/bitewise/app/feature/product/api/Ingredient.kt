package com.bitewise.app.feature.product.api

data class Ingredient (
    val name: String,
    val subIngredients: List<String> = emptyList(),
    val isRisky: Boolean
)
