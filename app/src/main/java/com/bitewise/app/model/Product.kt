package com.bitewise.app.model

data class Product(
    val code: String,
    val name: String,
    val imageUrl: String?,
    val calories: Float?,
    val fat: Float?,
    val saturatedFat: Float?,
    val carbohydrates: Float?,
    val sugars: Float?,
    val proteins: Float?,
    val salt: Float?,
    val nutritionGrade: String?,
    val ecoScoreGrade: String?,
    val novaGroup: Int?
)
