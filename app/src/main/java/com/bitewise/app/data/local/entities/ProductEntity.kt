package com.bitewise.app.data.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(

    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,

    @ColumnInfo(name = "product_name")
    val productName: String?,

    @ColumnInfo(name = "brands")
    val brands: String?,

    @ColumnInfo(name = "image_front_url")
    val imageFrontUrl: String?,

    @ColumnInfo(name = "nutriscore_grade")
    val nutriscoreGrade: String?,

    @ColumnInfo(name = "nova_group")
    val novaGroup: Int?,

    @ColumnInfo(name = "categories_tags")
    val categoriesTags: String?,

    @ColumnInfo(name = "search_text")
    val searchText: String?,

    @ColumnInfo(name = "allergens_tags")
    val allergenTags: String?,

    @ColumnInfo(name = "nutriments_json")
    val nutriments: String?

    //TODO("encapsulate entity fields based on relation. e.g. ProductScoresEntity, NutritionEntity, etc...")
)
