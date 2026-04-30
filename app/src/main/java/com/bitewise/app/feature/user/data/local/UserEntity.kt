package com.bitewise.app.feature.user.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @ColumnInfo(name = "age")
    val age: Int,

    @ColumnInfo(name = "weight")
    val weight: Double,

    @ColumnInfo(name = "height")
    val height: Double,

    @ColumnInfo(name = "activity")
    val activity: String,

    @ColumnInfo(name = "conditions")
    val conditions: String,

    @ColumnInfo(name = "dietary")
    val dietary: String,

    @ColumnInfo(name = "allergies")
    val allergies: String
)
