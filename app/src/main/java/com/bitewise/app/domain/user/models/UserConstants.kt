package com.bitewise.app.domain.user.models

object UserConstants {
    val ALLERGIES = listOf(
        "Milk", "Eggs", "Peanuts", "Tree Nuts", "Fish", "Shellfish", 
        "Wheat", "Soy", "Sesame", "Corn", "Gluten", "Mustard", "Sulfites"
    ).sorted()

    val DIET_TYPES = listOf(
        "OMNIVORE", "VEGETARIAN", "VEGAN", "PESCATARIAN", "KETO", 
        "PALEO", "LOW_CARB", "DASH", "MEDITERRANEAN", "HALAL", "KOSHER"
    ).sorted()

    val MEDICAL_CONDITIONS = listOf(
        "Diabetes Type 1", "Diabetes Type 2", "Hypertension", "Celiac Disease",
        "IBS", "GERD", "Lactose Intolerance", "High Cholesterol", "Kidney Disease"
    ).sorted()

    val ACTIVITY_LEVELS = mapOf(
        "SEDENTARY" to "Little or no exercise, desk job",
        "LIGHTLY_ACTIVE" to "Light exercise or sports 1-3 days a week",
        "MODERATELY_ACTIVE" to "Moderate exercise or sports 3-5 days a week",
        "VERY_ACTIVE" to "Hard exercise or sports 6-7 days a week",
        "EXTRA_ACTIVE" to "Very hard exercise, physical job or training twice a day"
    )
}
