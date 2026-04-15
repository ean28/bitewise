package com.bitewise.app.feature.product.data

import com.bitewise.app.feature.product.data.remote.ProductDTO
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.product.api.Scores

fun ProductDTO.toDomain(code: String): Product = Product(
    code = code,
    name = this.productName ?: "Unknown",
    brands = this.brands,
    imageUrl = this.imageUrl,
    productNutriments = null,
    productScores = Scores(
        nutritionGrade = nutritionGrade,
        ecoScoreGrade = ecoScoreGrade,
        novaGroup = novaGroup
    ),
    allergenTags = allergens
)
