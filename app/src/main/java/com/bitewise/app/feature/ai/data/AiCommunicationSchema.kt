package com.bitewise.app.feature.ai.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
object AiCommunicationSchema {

    //
    // [ SYSTEM PROMPT ]
    //
    val SYSTEM_PROMPT =
        """
        You are the BiteWise expert nutrition assistant. Your goal is to analyze a batch of food products for a specific user.
        
        [ USER_CONTEXT ]
        The user's health profile is provided in the 'u' object:
        $USER_CONTEXT_BLUEPRINT
        
        [ DATA_PAYLOAD ]
        The products to analyze are provided in the 'ps' array:
        $PRODUCT_DATA_BLUEPRINT
        
        [ RESPONSE_FORMAT ]
        You MUST return valid JSON identical to this structure:
        $RESPONSE_BLUEPRINT
        
        [ RULES ]
        1. SAFETY: If an ingredient in 'ing' or manufacturer tag in 'alg' matches any entry in user's 'alg' list, the score MUST be 0.
        2. CONCISE: Keep 'summary' to 1-2 short sentences.
        3. DATA: Do not invent products. Only analyze the ones in 'ps'.
    """.trimIndent()

    //
    // [ BLUEPRINTS ]
    //
    private const val USER_CONTEXT_BLUEPRINT = """
        {
          "age": Int,
          "w": Double (Weight kg),
          "h": Double (Height cm),
          "act": String (Activity Level),
          "cond": [String] (Conditions),
          "alg": [String] (Allergies),
          "diet": String (Dietary Type)
        }
    """

    private const val PRODUCT_DATA_BLUEPRINT = """
        [
          {
            "id": String (Barcode),
            "n": String (Product Name),
            "nut": { "en": kcal, "su": sugar, "sa": salt, "fa": fat, "sf": sat-fat, "pr": protein },
            "ing": String (Ingredients),
            "alg": String (Manufacturer Allergens),
            "ph": String (Product Hash)
          }
        ]
    """

    private const val RESPONSE_BLUEPRINT = """
        {
          "results": {
            "barcode_id": {
              "score": Double (0-100),
              "tags": [String] (2-4 tags),
              "summary": String (Concise reasoning)
            }
          }
        }
    """

    // =========================
    // IMPLEMENTATION MODELS
    // =========================

    @Serializable
    data class BatchRequest(
        @SerialName("u") val user: UserPayload,
        @SerialName("ps") val products: List<ProductPayload>
    )

    @Serializable
    data class UserPayload(
        @SerialName("age") val age: Int,
        @SerialName("w") val weight: Double,
        @SerialName("h") val height: Double,
        @SerialName("act") val activity: String,
        @SerialName("cond") val conditions: List<String>,
        @SerialName("alg") val allergies: List<String>,
        @SerialName("diet") val diet: String
    )

    @Serializable
    data class ProductPayload(
        @SerialName("id") val id: String,
        @SerialName("n") val name: String,
        @SerialName("nut") val nutrients: Map<String, String>,
        @SerialName("ing") val ingredients: String,
        @SerialName("alg") val allergens: String,
        @SerialName("ph") val productHash: Int
    )

    @Serializable
    data class BatchResponse(
        val results: Map<String, ProductAnalysis>
    )

    @Serializable
    data class ProductAnalysis(
        val score: Double,
        val tags: List<String>,
        val summary: String
    )
}
