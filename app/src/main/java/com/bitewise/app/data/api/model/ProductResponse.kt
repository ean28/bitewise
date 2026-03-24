package com.bitewise.app.data.api.model

import com.google.gson.annotations.SerializedName

data class ProductResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("product")
    val product: ProductDTO?,
    @SerializedName("status")
    val status: Int
)
