package com.bitewise.app.domain

data class Product(
    val code: String,
    val name: String,
    val imageUrl: String?,

    val productNutriments: Nutriments?,
    val productScores: Scores?,
    val allergenTags: String?,

)