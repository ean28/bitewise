package com.bitewise.app.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDTO(
    @SerialName("product_name")
    val productName: String?,

    @SerialName("brands")
    val brands: String?,

    @SerialName("image_front_url")
    val imageUrl: String?,

    @SerialName("allergens")
    val allergens: String?,

    @SerialName("serving_size")
    val servingSize: String?,

    @SerialName("nutriments")
    val nutriments: NutrimentsDTO?,

    @SerialName("nutriscore_grade")
    val nutritionGrade: String?,

    @SerialName("ecoscore_grade")
    val ecoScoreGrade: String?,

    @SerialName("nova_group")
    val novaGroup: Int?
)
