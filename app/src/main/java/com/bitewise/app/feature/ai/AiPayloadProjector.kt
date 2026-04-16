package com.bitewise.app.feature.ai

import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.feature.ai.data.AiCommunicationSchema
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.Locale

class AiPayloadProjector {
    private val json = Json {
        encodeDefaults = false
        ignoreUnknownKeys = true
    }

    private val nutrientMap = mapOf(
        "energy" to "en",
        "sugars" to "su",
        "carbohydrates" to "ca",
        "fiber" to "fi",
        "proteins" to "pr",
        "fat" to "fa",
        "saturated-fat" to "sf",
        "salt" to "sa",
        "sodium" to "so",
        "vitamin a" to "va",
        "vitamin c" to "vc",
        "vitamin d" to "vd",
        "vitamin e" to "ve",
        "vitamin k" to "vk",
        "vitamin b12" to "b1",
        "calcium" to "cl",
        "iron" to "ir",
        "potassium" to "po",
        "magnesium" to "ma",
        "zinc" to "zi",
        "cholesterol" to "ch",
        "trans-fat" to "tf"
    )

    fun createBatchPayload(products: List<Product>, user: UserContext): String {
        val userPayload = AiCommunicationSchema.UserPayload(
            age = user.age,
            weight = user.weight,
            height = user.height,
            activity = user.activity,
            conditions = user.conditions,
            allergies = user.allergies,
            diet = user.dietType
        )

        val productsPayload = products.map { product ->
            val nutrients = mutableMapOf<String, String>()
            product.productNutriments?.forEach { nut ->
                val key = mapNutrientKey(nut.name)
                val normalized = normalizeToGrams(nut.name, nut.amount, nut.unit)
                if (normalized > 0 || isCriticalNutrient(nut.name)) {
                    nutrients[key] = formatDouble(normalized.toDouble())
                }
            }

            AiCommunicationSchema.ProductPayload(
                id = product.code,
                name = product.name,
                nutrients = nutrients,
                ingredients = trimIngredients(product.ingredientsText ?: ""),
                allergens = product.allergenTags ?: "",
                productHash = product.hashCode()
            )
        }

        val request = AiCommunicationSchema.BatchRequest(
            user = userPayload,
            products = productsPayload
        )

        return json.encodeToString(request)
    }

    private fun mapNutrientKey(name: String): String {
        val lower = name.lowercase().replace(" ", "-")
        return nutrientMap[lower] ?: lower.take(3).filter { it.isLetter() }
    }

    private fun formatDouble(value: Double): String {
        return String.format(Locale.US, "%.4f", value).trimEnd('0').trimEnd('.')
    }

    private fun normalizeToGrams(name: String, amount: Float, unit: String): Float {
        return when (unit.lowercase()) {
            "mg" -> amount / 1000f
            "µg", "ug" -> amount / 1000000f
            "kg" -> amount * 1000f
            else -> amount
        }
    }

    private fun isCriticalNutrient(name: String): Boolean {
        val critical = listOf("suga", "salt", "fat", "satu", "sodi")
        return critical.any { name.lowercase().contains(it) }
    }

    private fun trimIngredients(text: String): String {
        val keywords = listOf("oil", "syrup", "acid", "gum", "flavor", "dye", "color", "extract", "modified")
        if (text.length <= 400) return text

        val words = text.split(",").map { it.trim() }
        val prioritized = words.filter { word ->
            keywords.any { word.contains(it, ignoreCase = true) }
        }

        val result = mutableListOf<String>()
        var currentLength = 0
        
        for (i in 0 until minOf(words.size, 5)) {
            result.add(words[i])
            currentLength += words[i].length
        }

        // Add prioritized words
        for (word in prioritized) {
            if (!result.contains(word) && currentLength + word.length < 350) {
                result.add(word)
                currentLength += word.length
            }
        }

        return result.joinToString(", ").take(400)
    }
}
