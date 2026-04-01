package com.bitewise.app.data.mapper

import com.bitewise.app.domain.models.Nutrients
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

object NutrientsMapper {

    fun mapJsonToNutrientList(nutrimentsMap: Map<String, JsonElement>?): List<Nutrients> {
        if (nutrimentsMap == null) return emptyList()

        val excludeKeywords = listOf("nova", "score", "estimate", "computed", "modifier", "label")
        val nutrientList = mutableListOf<Nutrients>()

        // We check for both 'energy-kj' and the generic 'energy' key which often holds the kJ value
        val energyKj = (nutrimentsMap["energy-kj_100g"] ?: nutrimentsMap["energy_100g"])
            ?.jsonPrimitive?.contentOrNull?.toFloatOrNull()

        val energyKcal = nutrimentsMap["energy-kcal_100g"]
            ?.jsonPrimitive?.contentOrNull?.toFloatOrNull()


        // Only add Energy if at least one of them is actually found (not null)
        if (energyKj != null || energyKcal != null) {
            nutrientList.add(
                Nutrients(
                    name = "Energy",
                    amount = energyKj ?: 0f,
                    // Use "---" or "0" only if the specific kcal is missing
                    unit = "kJ <br><i>(${energyKcal?.toInt() ?: "---"} kcal)</i>"
                )
            )
        }

        // 2. Map everything else
        val otherNutrients = nutrimentsMap.entries
            .asSequence()
            .filter { it.key.endsWith("_100g") }
            .filter { (key, _) ->
                excludeKeywords.none { key.contains(it) } &&
                        !key.startsWith("energy")
            }
            .mapNotNull { (key, element) ->
                val amount = element.jsonPrimitive.contentOrNull?.toFloatOrNull() ?: return@mapNotNull null

                val baseKey = key.removeSuffix("_100g")
                val cleanName = baseKey.replace("-", " ")
                    .split(" ")
                    .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

                val unit = nutrimentsMap["${baseKey}_unit"]?.jsonPrimitive?.contentOrNull ?: "g"

                Nutrients(name = cleanName, amount = amount, unit = unit)
            }
            .sortedByDescending { it.amount }
            .toList()

        nutrientList.addAll(otherNutrients)
        return nutrientList
    }
}