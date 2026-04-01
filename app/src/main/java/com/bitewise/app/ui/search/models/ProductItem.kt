package com.bitewise.app.ui.search.models

data class ProductItem (
    val id: String,
    val name: String,
    val imageUrl: Int,
    val nutriScore: String,
    val ecoScore: String,
    val novaScore: Int
)