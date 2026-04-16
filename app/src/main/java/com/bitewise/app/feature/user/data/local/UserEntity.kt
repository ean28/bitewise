package com.bitewise.app.feature.user.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserEntity(
    @PrimaryKey val id: Int = 0,
    val age: Int,
    val weight: Double,
    val height: Double,
    val activity: String,
    val conditions: String, // Comma-separated
    val dietary: String, // Comma-separated
    val allergies: String // Comma-separated
)
