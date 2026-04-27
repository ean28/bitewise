package com.bitewise.app.feature.onboarding

import android.content.Context
import androidx.core.content.edit
import com.bitewise.app.core.Constants

object OnboardingManager {

    fun isOnboardingComplete(context: Context): Boolean {
        val prefs = context.getSharedPreferences(Constants.PREFS_ONBOARDING, Context.MODE_PRIVATE)
        return prefs.getBoolean(Constants.KEY_ONBOARDING_DONE, false)
    }

    fun setOnboardingComplete(context: Context, complete: Boolean = true) {
        val prefs = context.getSharedPreferences(Constants.PREFS_ONBOARDING, Context.MODE_PRIVATE)
        prefs.edit { putBoolean(Constants.KEY_ONBOARDING_DONE, complete) }
    }

    fun resetOnboarding(context: Context) {
        setOnboardingComplete(context, false)
    }
}
