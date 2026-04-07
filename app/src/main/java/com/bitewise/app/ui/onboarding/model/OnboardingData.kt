package com.bitewise.app.ui.onboarding.model

data class OnboardingData(
    var age: Int? = null,
    var weight: Float? = null,
    var height: Float? = null,
    var activityLevel: String? = null,
    var medicalConditions: String? = null,
    var allergies: String? = null,
    var dietaryPreferences: DietType
)


enum class DietType { VEGAN, VEGETARIAN, OMNIVORE, KETO }