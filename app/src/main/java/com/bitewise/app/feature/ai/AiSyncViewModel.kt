package com.bitewise.app.feature.ai

import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.bitewise.app.feature.ai.data.local.AiSyncLog
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.domain.user.repository.UserRepository
import com.bitewise.app.feature.ai.data.AiConfiguration
import com.bitewise.app.feature.ai.data.AiTokenEstimator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AiSyncViewModel(
    private val aiRepository: AiRepository,
    private val userRepository: UserRepository,
    private val workManager: WorkManager,
    context: Context
) : ViewModel() {

    private val prefs = context.applicationContext.getSharedPreferences(AiConfiguration.PREFS_NAME, Context.MODE_PRIVATE)

    private val _batchSize = MutableStateFlow(prefs.getInt(AiConfiguration.KEY_BATCH_SIZE, 80))
    val batchSize: StateFlow<Int> = _batchSize.asStateFlow()

    private val _maxSliderValue = MutableStateFlow(80)
    val maxSliderValue: StateFlow<Int> = _maxSliderValue.asStateFlow()

    private val _quickSyncLimit = MutableStateFlow(prefs.getInt("ai_quick_sync_limit", 10))

    private val _hasInterruptedSync = MutableStateFlow(prefs.contains(AiConfiguration.KEY_LAST_BARCODE))
    val hasInterruptedSync: StateFlow<Boolean> = _hasInterruptedSync.asStateFlow()

    val syncLogs: StateFlow<List<AiSyncLog>> = aiRepository.getRecentSyncLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workInfos: StateFlow<List<WorkInfo>> = workManager.getWorkInfosByTagFlow("AiSync")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val uiState: StateFlow<SyncPreFlightState> = combine(
        aiRepository.getAnalyzedCountFlow(),
        userRepository.getUserContext(),
        _batchSize
    ) { _, user, currentBatchSize ->
        if (user == null || !user.isComplete()) {
            SyncPreFlightState()
        } else {
            val hash = user.hashCode()
            val stale = aiRepository.getStaleItemCount(hash)
            val needingSync = aiRepository.getItemsNeedingSyncCount(hash)
            val fresh = aiRepository.getFreshItemCount(hash)

            val totalToProcess = stale + needingSync
            val maxVal = totalToProcess.coerceAtLeast(80)

            SyncPreFlightState(
                freshCount = fresh,
                staleCount = stale,
                needAnalysis = needingSync,
                estTokensPerBatch = AiTokenEstimator.estimateSingleBatchTokens(currentBatchSize, user),
                totalEstTokens = AiTokenEstimator.estimateTotalSyncTokens(totalToProcess, currentBatchSize, user),
                estDurationSeconds = AiTokenEstimator.estimateDurationSeconds(totalToProcess, currentBatchSize),
                maxBatchSize = maxVal
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SyncPreFlightState())

    val isUserComplete: StateFlow<Boolean> = userRepository.getUserContext()
        .map { it?.isComplete() == true }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    init {
        observeSettings()
        observeUiState()
    }

    private fun observeSettings() {
        viewModelScope.launch {
            _batchSize.collectLatest { size ->
                prefs.edit { putInt(AiConfiguration.KEY_BATCH_SIZE, size) }
            }
        }
        viewModelScope.launch {
            _quickSyncLimit.collectLatest { limit ->
                prefs.edit { putInt("ai_quick_sync_limit", limit) }
            }
        }
    }

    private fun observeUiState() {
        viewModelScope.launch {
            uiState.collectLatest { state ->
                _maxSliderValue.value = state.maxBatchSize
                if (_batchSize.value > state.maxBatchSize) {
                    _batchSize.value = state.maxBatchSize
                }
            }
        }
    }

    fun setBatchSize(size: Int) {
        val max = _maxSliderValue.value

        val snappedValue = when {
            size >= max -> max
            else -> (size / 10) * 10
        }
        _batchSize.value = snappedValue.coerceIn(80, max.coerceAtLeast(80))
    }

    fun setQuickSyncLimit(limit: Int) {
        _quickSyncLimit.value = limit
    }

    fun startSync(isQuick: Boolean = false) {
        if (!isUserComplete.value) return

        if (!hasInterruptedSync.value) {
            prefs.edit { remove(AiConfiguration.KEY_LAST_BARCODE) }
        }

        val limit = if (isQuick) _quickSyncLimit.value else _batchSize.value
        aiRepository.triggerSync(limit)
        _hasInterruptedSync.value = false
    }

    fun resumeSync() {
        aiRepository.triggerSync(null)
        _hasInterruptedSync.value = false
    }

    fun cancelInterruptedSync() {
        prefs.edit { remove(AiConfiguration.KEY_LAST_BARCODE) }
        _hasInterruptedSync.value = false
    }

    fun stopSync() {
        workManager.cancelUniqueWork("AiBatchSync")
    }

    fun clearCache() {
        viewModelScope.launch {
            val user = userRepository.getUserContext().first()
            user?.let {
                Log.d("AiSyncViewModel", "Forcing refresh of cache for hash: ${it.hashCode()}")
                aiRepository.forceRefreshCache(it.hashCode())

                _hasInterruptedSync.value = false
                prefs.edit { remove(AiConfiguration.KEY_LAST_BARCODE) }
            }
        }
    }
}

data class SyncPreFlightState(
    val freshCount: Int = 0,
    val staleCount: Int = 0,
    val needAnalysis: Int = 0,
    val estTokensPerBatch: Int = 0,
    val totalEstTokens: Int = 0,
    val estDurationSeconds: Int = 0,
    val maxBatchSize: Int = 150
)
