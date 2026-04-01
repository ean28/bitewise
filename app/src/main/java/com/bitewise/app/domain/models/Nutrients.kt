package com.bitewise.app.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Nutrients(
    val name: String,
    val amount: Float,
    val unit: String
)