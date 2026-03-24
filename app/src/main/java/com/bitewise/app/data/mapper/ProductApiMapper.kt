package com.bitewise.app.data.mapper

import com.bitewise.app.data.api.model.ProductDTO
import com.bitewise.app.model.Product

fun ProductDTO.toDomain(code: String): Product = Product(
    code = code,
    name = this.productName ?: "Unknown",
    imageUrl = this.imageUrl,
    calories = this.nutriments?.energyKcal,
    fat = this.nutriments?.fat,
    saturatedFat = this.nutriments?.saturatedFat,
    carbohydrates = this.nutriments?.carbohydrates,
    sugars = this.nutriments?.sugars,
    proteins = this.nutriments?.proteins,
    salt = this.nutriments?.salt,
    nutritionGrade = this.nutritionGrade,
    ecoScoreGrade = this.ecoScoreGrade,
    novaGroup = this.novaGroup
)
