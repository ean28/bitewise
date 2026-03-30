package com.bitewise.app.data.mapper

import com.bitewise.app.data.local.entities.ProductEntity
import com.bitewise.app.domain.Product
import com.bitewise.app.domain.Scores

fun ProductEntity.toDomain(): Product = Product(
    code = this.code,
    name = this.productName ?: "Unknown",
    imageUrl = this.imageFrontUrl,
    productScores = Scores(
        nutritionGrade = nutriscoreGrade,
        ecoScoreGrade = null,
        novaGroup = novaGroup
    ),
    allergenTags = this.allergenTags,
    productNutriments = null
)

//    calories = null,
//    fat = null,
//    saturatedFat = null,
//    carbohydrates = null,
//    sugars = null,
//    proteins = null,
//    salt = null,

//TODO("Refactor based on changes in product domain")

fun Product.toEntity(): ProductEntity = ProductEntity(
    code = code,
    productName = name,
    brands = null,
    imageFrontUrl = imageUrl,
    nutriscoreGrade = productScores?.nutritionGrade,
    novaGroup = productScores?.novaGroup,
    categoriesTags = null,
    searchText = null,
    allergenTags = allergenTags,
    nutriments = null
)
