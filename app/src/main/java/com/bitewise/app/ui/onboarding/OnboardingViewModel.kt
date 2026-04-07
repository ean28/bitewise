package com.bitewise.app.ui.onboarding

import androidx.lifecycle.ViewModel
import com.bitewise.app.ui.onboarding.model.OnboardingData

class OnboardingViewModel : ViewModel() {

    val data = OnboardingData()

    fun isPhysicalValid(): Boolean {
        return data.age != null &&
                data.weight != null &&
                data.height != null
    }

    fun isLifestyleValid(): Boolean {
        return !data.activityLevel.isNullOrBlank()
    }

    fun isHealthValid(): Boolean {
        return true
    }
}