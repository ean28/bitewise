package com.bitewise.app.data.network.model

import com.google.gson.annotations.SerializedName

data class Nutriments(
    @SerializedName("energy-kcal")
    val energyKcal: Float?,
    @SerializedName("fat")
    val fat: Float?,
    @SerializedName("saturated-fat")
    val saturatedFat: Float?,
    @SerializedName("carbohydrates")
    val carbohydrates: Float?,
    @SerializedName("sugars")
    val sugars: Float?,
    @SerializedName("proteins")
    val proteins: Float?,
    @SerializedName("salt")
    val salt: Float?
)