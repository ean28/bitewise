package com.bitewise.app.data.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NutrimentsDTO(
    @SerialName(value = "energy-kcal")
    val energyKcal: Float? = null,
    @SerialName(value = "energy-kcal_prepared")
    val energyKcalPrepared: Float? = null,

    @SerialName(value = "fat")
    val fat: Float? = null,
    @SerialName(value = "fat_prepared")
    val fatPrepared: Float? = null,

    @SerialName(value = "saturated-fat")
    val saturatedFat: Float? = null,
    @SerialName(value = "saturated-fat_prepared")
    val saturatedFatPrepared: Float? = null,

    @SerialName(value = "carbohydrates")
    val carbohydrates: Float? = null,
    @SerialName(value = "carbohydrates_prepared")
    val carbohydratesPrepared: Float? = null,

    @SerialName(value = "sugars")
    val sugars: Float? = null,
    @SerialName(value = "sugars_prepared")
    val sugarsPrepared: Float? = null,

    @SerialName(value = "proteins")
    val proteins: Float? = null,
    @SerialName(value = "proteins_prepared")
    val proteinsPrepared: Float? = null,

    @SerialName(value = "salt")
    val salt: Float? = null,
    @SerialName(value = "salt_prepared")
    val saltPrepared: Float? = null
)