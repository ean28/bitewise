package com.bitewise.app.domain.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getBatchSize(): Flow<Int>
    suspend fun updateBatchSize(size: Int)
    fun isOnboardingDone(): Flow<Boolean>
    suspend fun setOnboardingDone(done: Boolean)
}
