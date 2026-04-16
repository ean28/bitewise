package com.bitewise.app.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _userContext = MutableStateFlow<UserContext?>(null)
    val userContext: StateFlow<UserContext?> = _userContext.asStateFlow()

    init {
        observeUserContext()
    }

    private fun observeUserContext() {
        viewModelScope.launch {
            userRepository.getUserContext().collectLatest {
                _userContext.value = it
            }
        }
    }

    fun saveUserContext(context: UserContext) {
        viewModelScope.launch {
            userRepository.saveUserContext(context)
        }
    }
}
