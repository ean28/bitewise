package com.bitewise.app.data.mapper

import com.bitewise.app.data.api.model.NutrimentsDTO
import com.bitewise.app.data.api.model.ProductDTO
import com.bitewise.app.domain.Nutriments
import com.bitewise.app.domain.Product
import com.bitewise.app.domain.Scores
import com.google.android.material.color.utilities.Score

fun ProductDTO.toDomain(code: String): Product = Product(
    code = code,
    name = this.productName ?: "Unknown",
    imageUrl = this.imageUrl,
    productNutriments = nutriments?.toDomain(),
    productScores = Scores(
        nutritionGrade = nutritionGrade,
        ecoScoreGrade = ecoScoreGrade,
        novaGroup = novaGroup
    ),
    allergenTags = allergens
)

fun NutrimentsDTO.toDomain(): Nutriments = Nutriments(
    energyKcal = energyKcal ?: energyKcalPrepared,
    fat = fat ?: fatPrepared,
    saturatedFat = saturatedFat ?: saturatedFatPrepared,
    carbohydrates = carbohydrates ?: carbohydratesPrepared,
    sugars = sugars ?: sugarsPrepared,
    proteins = proteins ?: proteinsPrepared,
    salt = salt ?: saltPrepared,
)
