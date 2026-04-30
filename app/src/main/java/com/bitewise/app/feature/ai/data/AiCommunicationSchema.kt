package com.bitewise.app.feature.ai.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
object AiCommunicationSchema {

    //
    // [ SYSTEM PROMPT ]
    //
    val SYSTEM_PROMPT =
        """
        You are the BiteWise expert nutrition assistant. Evaluate food products and assign a 0–100 health suitability score based on the user's profile.
        
        [ SCORING ]
        Start from ~50 and adjust using:
        
        1. SAFETY (Critical)
        - If any ingredient or allergen suggests presence of user's allergens → score = 0
        - Be cautious with partial or unclear matches
        
        2. USER ALIGNMENT (High)
        - Evaluate alignment with diet and health conditions
        - Apply proportional penalties/rewards (no hard thresholds unless extreme)
        
        3. NUTRITIONAL BALANCE (High)
        - Sugar, unhealthy fats, salt → negative
        - Protein, fiber, whole ingredients → positive
        - Evaluate in context, not isolation
        
        4. PROCESSING (Moderate)
        - Ultra-processed, additives, refined ingredients → slight penalty
        - Whole/minimally processed → slight reward
        
        [ DATA ]
        - Values are per 100g/ml
        - Keys:
          "en" kcal, "su" sugar, "ca" carbs, "fi" fiber, "pr" protein,
          "fa" fat, "sf" sat fat, "sa" salt, "so" sodium,
          "ch" cholesterol, "tf" trans fat
        - Missing data → do not assume
        
        [ CALIBRATION ]
        - Score comparatively within the batch
        - Reflect meaningful differences
        - Avoid clustering or unjustified large gaps
        
        [ CONSISTENCY ]
        - Similar products → similar scores/tags
        - Avoid random variation
        
        [ DATA QUALITY ]
        - Limited data → avoid extreme scores, lean moderate
        
        [ STABILITY ]
        - Do not over-stack penalties
        - Merge related negatives into one stronger effect
        - Keep reasonable distribution unless clearly harmful
        
        [ USER_CONTEXT ]
        $USER_CONTEXT_BLUEPRINT
        
        [ DATA_PAYLOAD ]
        $PRODUCT_DATA_BLUEPRINT
        
        [ RESPONSE ]
        Return ONLY valid JSON:
        $RESPONSE_BLUEPRINT
        
        [ RULES ]
        - Scores must be realistic and consistent
        - Avoid extremes unless justified
        - Tags: 3–6 concise labels
        - Summary: concise, information-dense, no filler
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
          "diet": [String] (Dietary Type)
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
              "tags": [String] (3-6 tags),
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
        @SerialName("u")
        val user: UserPayload,

        @SerialName("ps")
        val products: List<ProductPayload>
    )

    @Serializable
    data class UserPayload(
        @SerialName("age")
        val age: Int,

        @SerialName("w")
        val weight: Double,

        @SerialName("h")
        val height: Double,

        @SerialName("act")
        val activity: String,

        @SerialName("cond")
        val conditions: List<String>,

        @SerialName("alg")
        val allergies: List<String>,

        @SerialName("diet")
        val diet: String
    )

    @Serializable
    data class ProductPayload(
        @SerialName("id")
        val id: String,

        @SerialName("n")
        val name: String,

        @SerialName("nut")
        val nutrients: Map<String, String>,

        @SerialName("ing")
        val ingredients: String,

        @SerialName("alg")
        val allergens: String,

        @SerialName("ph")
        val productHash: Int
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
