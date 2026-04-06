package com.bitewise.app.domain.models

data class Ingredient (
    val name: String,
    val subIngredients: List<String> = emptyList(),
    val isRisky: Boolean
)