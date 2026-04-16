package com.bitewise.app.feature.product.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.json.JsonElement

@Entity(tableName = "products")
@TypeConverters(NutrimentsConverter::class)
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
    val nutriments: Map<String, JsonElement>?,

    @ColumnInfo(name = "ecoscore_grade")
    val ecoScoreGrade: String?,

    @ColumnInfo(name = "ingredients_text")
    val ingredientsText: String?,

    @ColumnInfo(name = "labels")
    val labels: String?
)
