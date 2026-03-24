package com.bitewise.app.data.api.model

import com.google.gson.annotations.SerializedName

data class NutrimentsDTO(
    @SerializedName(value = "energy-kcal", alternate = ["energy-kcal_prepared"])
    val energyKcal: Float?,

    @SerializedName(value = "fat", alternate = ["fat_prepared"])
    val fat: Float?,

    @SerializedName(value = "saturated-fat", alternate = ["saturated-fat_prepared"])
    val saturatedFat: Float?,

    @SerializedName(value = "carbohydrates", alternate = ["carbohydrates_prepared"])
    val carbohydrates: Float?,

    @SerializedName(value = "sugars", alternate = ["sugars_prepared"])
    val sugars: Float?,

    @SerializedName(value = "proteins", alternate = ["proteins_prepared"])
    val proteins: Float?,

    @SerializedName(value = "salt", alternate = ["salt_prepared"])
    val salt: Float?
)
