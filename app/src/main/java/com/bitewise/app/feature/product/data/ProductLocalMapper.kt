package com.bitewise.app.feature.product.data

import com.bitewise.app.feature.product.data.local.ProductEntity
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.product.api.Scores

fun ProductEntity.toDomain(): Product = Product(
    code = this.code,
    name = this.productName ?: "Unknown",
    brands = this.brands,
    imageUrl = this.imageFrontUrl,
    productScores = Scores(
        nutritionGrade = nutriscoreGrade,
        ecoScoreGrade = ecoScoreGrade,
        novaGroup = novaGroup
    ),
    allergenTags = this.allergenTags,
    productNutriments = NutrientsProcessor.process(this.nutriments),
    ingredientsText = this.ingredientsText,
    productIngredients = IngredientsProcessor.process(this.ingredientsText),
    labels = this.labels
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    code = code,
    productName = name,
    brands = brands,
    imageFrontUrl = imageUrl,
    nutriscoreGrade = productScores?.nutritionGrade,
    novaGroup = productScores?.novaGroup,
    ecoScoreGrade = productScores?.ecoScoreGrade,
    categoriesTags = null,
    searchText = null,
    allergenTags = allergenTags,
    nutriments = null, // Note: Mapping back from domain nutrients to JSON is complex and usually not needed for read-only assets
    ingredientsText = ingredientsText,
    labels = labels
)
