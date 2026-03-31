package com.bitewise.app.domain

data class Product(
    val code: String,
    val name: String,
    val imageUrl: String?,

    val productNutriments: List<Nutrients>?,
    val productScores: Scores?,
    val allergenTags: String?,

)