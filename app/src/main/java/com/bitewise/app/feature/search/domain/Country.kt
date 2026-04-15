package com.bitewise.app.feature.search.domain

data class Country (
    val name: String,
    val flag: String
){
    override fun toString(): String = "$flag $name"
}