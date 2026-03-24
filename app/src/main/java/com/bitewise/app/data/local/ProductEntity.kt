package com.bitewise.app.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @ColumnInfo(name = "code")
    val code: String,

    @SerializedName("product_name")
    @ColumnInfo(name = "product_name")
    val product_name: String?,

    @SerializedName("brands")
    @ColumnInfo(name = "brands")
    val brands: String?,

    @SerializedName("image_front_url")
    @ColumnInfo(name = "image_front_url")
    val image_front_url: String?,

    @SerializedName("nutriscore_grade")
    @ColumnInfo(name = "nutriscore_grade")
    val nutriscore_grade: String?,

    @SerializedName("nova_group")
    @ColumnInfo(name = "nova_group")
    val nova_group: Int?,

    @SerializedName("categories_tags")
    @ColumnInfo(name = "categories_tags")
    val categories_tags: String?,

    @SerializedName("search_text")
    @ColumnInfo(name = "search_text")
    val search_text: String?
)
