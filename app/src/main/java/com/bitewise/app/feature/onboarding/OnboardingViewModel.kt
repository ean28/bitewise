package com.bitewise.app.feature.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.domain.settings.SettingsRepository
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import com.bitewise.app.feature.onboarding.domain.OnboardingData
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val data = OnboardingData()
    private val _onboardingCompleted = MutableSharedFlow<Unit>()
    val onboardingCompleted: SharedFlow<Unit> = _onboardingCompleted.asSharedFlow()

    fun isPhysicalValid(): Boolean {
        return data.age != null && data.weight != null && data.height != null
    }

    fun completeOnboarding() {
        viewModelScope.launch {
            val userContext = UserContext(
                age = data.age ?: 0,
                weight = data.weight?.toDouble() ?: 0.0,
                height = data.height?.toDouble() ?: 0.0,
                activity = data.activityLevel ?: "",
                conditions = data.medicalConditions,
                dietary = data.dietaryPreferences,
                allergies = data.allergies
            )
            userRepository.saveUserContext(userContext)
            settingsRepository.setOnboardingDone(true)
            _onboardingCompleted.emit(Unit)
        }
    }
}
