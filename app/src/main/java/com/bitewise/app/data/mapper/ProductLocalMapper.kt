package com.bitewise.app.data.mapper

import com.bitewise.app.data.local.entities.ProductEntity
import com.bitewise.app.domain.models.Product
import com.bitewise.app.domain.models.Scores

fun ProductEntity.toDomain(): Product = Product(
    code = this.code,
    name = this.productName ?: "Unknown",
    imageUrl = this.imageFrontUrl,
    productScores = Scores(
        nutritionGrade = nutriscoreGrade,
        ecoScoreGrade = ecoScoreGrade,
        novaGroup = novaGroup
    ),
    allergenTags = this.allergenTags,
    productNutriments = NutrientsProcessor.process(this.nutriments),
    productIngredients = IngredientsProcessor.process(this.ingredientsText),
    labels = this.labels
)
//TODO("Refactor based on changes in product domain")

fun Product.toEntity(): ProductEntity = ProductEntity(
    code = code,
    productName = name,
    brands = null,
    imageFrontUrl = imageUrl,
    nutriscoreGrade = productScores?.nutritionGrade,
    novaGroup = productScores?.novaGroup,
    ecoScoreGrade = productScores?.ecoScoreGrade,
    categoriesTags = null,
    searchText = null,
    allergenTags = allergenTags,
    nutriments = null,
    ingredientsText = null,
    labels = this.labels
)
