package com.bitewise.app.domain.models

data class Product(
    val code: String,
    val name: String,
    val imageUrl: String?,

    val productNutriments: List<Nutrient>?,
    val productScores: Scores?,
    val allergenTags: String?,
    val productIngredients: List<Ingredient>, // maybe list
    val labels: String?

)