package com.bitewise.app.data.api.constants

object ApiConstants {
    private val RAW_SEARCH_FIELDS =
        """
        code,
        product_name,
        brands,
        image_front_url,
        nutriments,
        nutrition_grades,
        allergens,
        serving_size, 
        nutriscore_grade,
        ecoscore_grade,
        countries_tags,
        nova_group
        """.trimIndent()

    val SEARCH_FIELDS = RAW_SEARCH_FIELDS.replace("\n", "").replace(" ", "")
}
