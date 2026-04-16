package com.bitewise.app.feature.settings.data

import android.content.Context
import androidx.core.content.edit
import com.bitewise.app.domain.settings.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LocalSettingsRepository(context: Context) : SettingsRepository {
    private val prefs = context.getSharedPreferences("ai_settings", Context.MODE_PRIVATE)
    private val onboardingPrefs = context.getSharedPreferences("onboarding_prefs", Context.MODE_PRIVATE)
    
    private val _batchSize = MutableStateFlow(prefs.getInt("ai_batch_size", 10))
    private val _onboardingDone = MutableStateFlow(onboardingPrefs.getBoolean("onboarding_done", false))
    
    override fun getBatchSize(): Flow<Int> = _batchSize.asStateFlow()

    override suspend fun updateBatchSize(size: Int) {
        prefs.edit { putInt("ai_batch_size", size) }
        _batchSize.value = size
    }

    override fun isOnboardingDone(): Flow<Boolean> = _onboardingDone.asStateFlow()

    override suspend fun setOnboardingDone(done: Boolean) {
        onboardingPrefs.edit { putBoolean("onboarding_done", done) }
        _onboardingDone.value = done
    }
}
