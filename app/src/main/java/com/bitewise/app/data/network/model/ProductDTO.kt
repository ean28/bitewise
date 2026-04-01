package com.bitewise.app.data.network.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("brands")
    val brands: String?,
    @SerializedName("image_front_url")
    val imageUrl: String?,
    @SerializedName("allergens")
    val allergens: String?,
    @SerializedName("serving_size")
    val servingSize: String?,
    @SerializedName("nutriments")
    val nutriments: Nutriments,
    @SerializedName("nutriscore_grade")
    val nutritionGrade: String?,
    @SerializedName("ecoscore_grade")
    val ecoScoreGrade: String?,
    @SerializedName("nova_group")
    val novaGroup: Int?
)