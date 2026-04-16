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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AiSyncViewModel(
    private val aiRepository: AiRepository,
    private val userRepository: UserRepository,
    private val workManager: WorkManager,
    context: Context
) : ViewModel() {

    private val prefs = context.applicationContext.getSharedPreferences(AiConfiguration.PREFS_NAME, Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(SyncPreFlightState())
    val uiState: StateFlow<SyncPreFlightState> = _uiState.asStateFlow()

    private val _batchSize = MutableStateFlow(prefs.getInt(AiConfiguration.KEY_BATCH_SIZE, AiConfiguration.DEFAULT_BATCH_SIZE))
//    val batchSize: StateFlow<Int> = _batchSize.asStateFlow()

    private val _quickSyncLimit = MutableStateFlow(prefs.getInt("ai_quick_sync_limit", 10))
    //val quickSyncLimit: StateFlow<Int> = _quickSyncLimit.asStateFlow()

    private val _isUserComplete = MutableStateFlow(false)
    val isUserComplete: StateFlow<Boolean> = _isUserComplete.asStateFlow()

    private val _hasInterruptedSync = MutableStateFlow(false)
    val hasInterruptedSync: StateFlow<Boolean> = _hasInterruptedSync.asStateFlow()

    val syncLogs: StateFlow<List<AiSyncLog>> = aiRepository.getRecentSyncLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val workInfos: StateFlow<List<WorkInfo>> = workManager.getWorkInfosByTagFlow("AiSync")
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        loadPreFlightStats()
        observeSettings()
        checkUserCompleteness()
        checkInterruptedSync()
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

    private fun checkUserCompleteness() {
        viewModelScope.launch {
            userRepository.getUserContext().collectLatest { user ->
                _isUserComplete.value = user?.isComplete() == true
            }
        }
    }

    private fun checkInterruptedSync() {
        _hasInterruptedSync.value = prefs.contains(AiConfiguration.KEY_LAST_BARCODE)
    }

    fun setBatchSize(size: Int) {
        _batchSize.value = size
    }

    fun setQuickSyncLimit(limit: Int) {
        _quickSyncLimit.value = limit
    }

    fun loadPreFlightStats() {
        viewModelScope.launch {
            val user = userRepository.getUserContext().first() ?: return@launch
            val hash = user.hashCode()
            val stale = aiRepository.getStaleItemCount(hash)
            val fresh = aiRepository.getFreshItemCount(hash)
            
            // Estimating ~350 tokens per product (Input + Output)
            val estTokens = stale * 350
            
            _uiState.value = SyncPreFlightState(
                freshCount = fresh,
                staleCount = stale,
                estTokens = estTokens,
                estDuration = stale * 2 // ~2 seconds per item including delay
            )
        }
    }

    fun startSync(isQuick: Boolean = false) {
        if (!_isUserComplete.value) return
        
        if (!hasInterruptedSync.value) {
            prefs.edit { remove(AiConfiguration.KEY_LAST_BARCODE) }
        }
        
        val limit = if (isQuick) _quickSyncLimit.value else null
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
            if (user != null) {
                aiRepository.forceRefreshCache(user.hashCode())
            } else {
                //aiRepository.clearAllAnalyses()
                Log.d("AI_SYNC_FRAGMENT", "Cleared Cache")
            }
            //TODO("handle graceful clear")
            //cancelInterruptedSync()
            loadPreFlightStats()
        }
    }
}

data class SyncPreFlightState(
    val freshCount: Int = 0,
    val staleCount: Int = 0,
    val estTokens: Int = 0,
    val estDuration: Int = 0
)
