package com.bitewise.app.data.network.model

import com.google.gson.annotations.SerializedName

data class ProductResponse (
    @SerializedName("code")
    val code: String,
    @SerializedName("product")
    val product: Product?,
    @SerializedName("status")
    val status: Int
)