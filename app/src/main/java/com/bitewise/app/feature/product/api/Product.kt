package com.bitewise.app.feature.product.api

data class Product(
    val code: String,
    val name: String,
    val brands: String?,
    val imageUrl: String?,
    val productNutriments: List<Nutrient>?,
    val productScores: Scores?,
    val allergenTags: String?,
    val ingredientsText: String? = null,
    val labels: String? = null,
    val productIngredients: List<Ingredient> = emptyList()
)
