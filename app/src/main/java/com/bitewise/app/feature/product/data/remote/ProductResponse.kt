package com.bitewise.app.feature.product.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductResponse (
    @SerialName("code")
    val code: String,
    @SerialName("product")
    val product: ProductDTO?,
    @SerialName("status")
    val status: Int
)
