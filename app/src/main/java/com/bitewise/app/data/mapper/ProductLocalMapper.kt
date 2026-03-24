package com.bitewise.app.data.mapper

import com.bitewise.app.data.local.ProductEntity
import com.bitewise.app.model.Product

fun ProductEntity.toDomain(): Product = Product(
    code = this.code,
    name = this.product_name ?: "Unknown",
    imageUrl = this.image_front_url,
    calories = null,
    fat = null,
    saturatedFat = null,
    carbohydrates = null,
    sugars = null,
    proteins = null,
    salt = null,
    nutritionGrade = this.nutriscore_grade,
    ecoScoreGrade = null,
    novaGroup = this.nova_group
)

fun Product.toEntity(): ProductEntity = ProductEntity(
    code = this.code,
    product_name = this.name,
    brands = null,
    image_front_url = this.imageUrl,
    nutriscore_grade = this.nutritionGrade,
    nova_group = this.novaGroup,
    categories_tags = null,
    search_text = null
)
