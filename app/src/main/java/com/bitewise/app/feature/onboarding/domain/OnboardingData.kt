package com.bitewise.app.feature.onboarding.domain

data class OnboardingData(
    var age: Int? = null,
    var weight: Float? = null,
    var height: Float? = null,
    var activityLevel: String? = null,
    var medicalConditions: List<String> = emptyList(),
    var allergies: List<String> = emptyList(),
    var dietaryPreferences: List<String> = listOf("OMNIVORE")
)
