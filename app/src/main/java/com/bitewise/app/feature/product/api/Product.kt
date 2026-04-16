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
) {
    override fun hashCode(): Int {
        val raw = "${name}|${ingredientsText}|${productNutriments?.joinToString { "${it.name}:${it.amount}" }}"
        return raw.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Product

        if (code != other.code) return false
        if (name != other.name) return false
        if (brands != other.brands) return false
        if (imageUrl != other.imageUrl) return false
        if (productNutriments != other.productNutriments) return false
        if (productScores != other.productScores) return false
        if (allergenTags != other.allergenTags) return false
        if (ingredientsText != other.ingredientsText) return false
        if (labels != other.labels) return false
        if (productIngredients != other.productIngredients) return false

        return true
    }
}
