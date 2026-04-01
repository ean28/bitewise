package com.bitewise.app.data.local.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement

class NutrimentsConverter {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    @TypeConverter
    fun fromJson(value: String?): Map<String, JsonElement>? {
        return value?.let {
            try {
                json.decodeFromString<Map<String, JsonElement>>(it)
            } catch(e: Exception){
                null
            }
        }
    }

    @TypeConverter
    fun toJson(map: Map<String, JsonElement>?): String? {
        return map?.let{ json.encodeToString(it)}
    }
}