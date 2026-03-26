package com.bitewise.app.data.api.model

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
