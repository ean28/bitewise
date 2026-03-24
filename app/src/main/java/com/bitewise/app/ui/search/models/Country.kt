package com.bitewise.app.ui.search.models

data class Country (
    val name: String,
    val flag: String
){
    override fun toString(): String = "$flag $name"
}