package com.bitewise.app.feature.product.data.local

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class NutrimentsConverter {
    
    private val json = Json {
        ignoreUnknownKeys = true
        encodeDefaults = true
        coerceInputValues = true
    }

    @TypeConverter
    fun fromJson(value: String?): Map<String, JsonElement>? {
        if (value.isNullOrBlank()) return null
        return try {
            json.decodeFromString<Map<String, JsonElement>>(value)
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun toJson(map: Map<String, JsonElement>?): String? {
        if (map == null) return null
        return try {
            json.encodeToString(map)
        } catch (e: Exception) {
            null
        }
    }
}
