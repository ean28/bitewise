package com.bitewise.app.feature.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DatabaseInspectorViewModel(
    private val userRepository: UserRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    val userContext: StateFlow<UserContext?> = userRepository.getUserContext()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val aiAnalyses: StateFlow<List<AiAnalysisEntity>> = aiRepository.getAllAnalyses()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun clearAiAnalyses() {
        viewModelScope.launch {
            aiRepository.clearAllAnalyses()
        }
    }
}
