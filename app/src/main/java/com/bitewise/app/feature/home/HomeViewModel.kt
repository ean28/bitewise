package com.bitewise.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.feature.product.api.RecentProductRepository
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import com.bitewise.app.feature.ai.domain.HealthScoringEngine
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.core.UiState
import com.bitewise.app.feature.ai.api.ScoredProduct
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

enum class SafetyShieldStatus {
    SECURE, // Green
    STALE,  // Amber
    SYNCING // Pulsing TODO("NOT PULSING")
}

class HomeViewModel(
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository,
    private val healthScoringEngine: HealthScoringEngine,
    private val aiRepository: AiRepository,
    private val recentProductRepository: RecentProductRepository
) : ViewModel() {

    private val _recentProducts = MutableStateFlow<List<Product>>(emptyList())
    val recentProducts: StateFlow<List<Product>> = _recentProducts.asStateFlow()

    private val _recommendedProducts = MutableStateFlow<UiState<List<ScoredProduct>>>(UiState.Loading)
    val recommendedProducts: StateFlow<UiState<List<ScoredProduct>>> = _recommendedProducts.asStateFlow()

    private val _userContext = MutableStateFlow<UserContext?>(null)
    val userContext: StateFlow<UserContext?> = _userContext.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _safetyStatus = MutableStateFlow(SafetyShieldStatus.SECURE)
    val safetyStatus: StateFlow<SafetyShieldStatus> = _safetyStatus.asStateFlow()

    private val _showSyncPrompt = MutableStateFlow(false)
    val showSyncPrompt: StateFlow<Boolean> = _showSyncPrompt.asStateFlow()

    private val _isSyncPromptDismissed = MutableStateFlow(false)
    val isSyncPromptDismissed: StateFlow<Boolean> = _isSyncPromptDismissed.asStateFlow()

    init {
        observeUserContext()
        observeRecommendations()
        observeStaleState()
        observeSyncRequirement()
    }

    private fun observeUserContext() {
        viewModelScope.launch {
            userRepository.getUserContext().collectLatest { context ->
                _userContext.value = context
            }
        }
    }

    private fun observeSyncRequirement() {
        viewModelScope.launch {
            combine(
                userRepository.getUserContext(),
                aiRepository.getAnalyzedCountFlow(),
                _isSyncPromptDismissed
            ) { user, count, dismissed ->
                user != null && count == 0 && !dismissed
            }.collectLatest { show ->
                _showSyncPrompt.value = show
            }
        }
    }

    private fun observeRecommendations() {
        viewModelScope.launch {
            combine(
                aiRepository.getAnalyzedCountFlow(),
                _isSyncPromptDismissed,
                healthScoringEngine.observeRecommendations(limit = 20)
            ) { count, dismissed, recommendations ->
                when {
                    count > 0 -> recommendations.filter { it.analysis != null }
                    dismissed -> recommendations
                    else -> emptyList()
                }
            }.collectLatest { states ->
                _recommendedProducts.value = UiState.Success(states)
            }
        }
    }

    private fun observeStaleState() {
        viewModelScope.launch {
            userContext.collectLatest { user ->
                user?.let {
                    aiRepository.isCacheStale(it.hashCode()).collectLatest { isStale ->
                        _safetyStatus.value = when {
                            _isSyncing.value -> SafetyShieldStatus.SYNCING
                            isStale -> SafetyShieldStatus.STALE
                            else -> SafetyShieldStatus.SECURE
                        }
                        
                        if (isStale && !_isSyncing.value && _showSyncPrompt.value == false) {
                            triggerAiSync()
                        }
                    }
                }
            }
        }
    }

    private fun triggerAiSync() {
        _isSyncing.value = true
        _safetyStatus.value = SafetyShieldStatus.SYNCING
        aiRepository.triggerSync()
    }

    fun dismissSyncPrompt() {
        _isSyncPromptDismissed.value = true
    }

    fun manualRefresh() {
        viewModelScope.launch {
            _userContext.value?.let {
                aiRepository.forceRefreshCache(it.hashCode())
            }
        }
    }

    fun loadRecentItems() {
        viewModelScope.launch {
            val barcodes = recentProductRepository.getBarcodes()
            val products = barcodes.mapNotNull { code ->
                productRepository.getProductByBarcode(code)
            }
            _recentProducts.value = products
        }
    }
}
