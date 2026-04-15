package com.bitewise.app.core

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.feature.product.api.RecentProductRepository
import com.bitewise.app.domain.user.repository.UserRepository
import com.bitewise.app.feature.ai.domain.HealthScoringEngine
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.domain.settings.SettingsRepository
import com.bitewise.app.feature.home.HomeViewModel
import com.bitewise.app.feature.onboarding.OnboardingViewModel
import com.bitewise.app.feature.product.ui.ProductDetailViewModel
import com.bitewise.app.feature.search.SearchViewModel
import com.bitewise.app.feature.settings.SettingsViewModel
import com.bitewise.app.feature.settings.ui.DatabaseInspectorViewModel
import com.bitewise.app.feature.ai.AiSyncViewModel
class ViewModelFactory(
    private val application: Application,
    private val productRepository: ProductRepository,
    private val userRepository: UserRepository? = null,
    private val healthScoringEngine: HealthScoringEngine? = null,
    private val aiRepository: AiRepository? = null,
    private val recentProductRepository: RecentProductRepository? = null,
    private val settingsRepository: SettingsRepository? = null
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(productRepository) as T
            }
            modelClass.isAssignableFrom(ProductDetailViewModel::class.java) -> {
                ProductDetailViewModel(
                    productRepository,
                    recentProductRepository!!,
                    aiRepository!!

                ) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    productRepository, 
                    userRepository!!, 
                    healthScoringEngine!!, 
                    aiRepository!!,
                    recentProductRepository!!
                ) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(
                    userRepository!!,
                ) as T
            }
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> {
                OnboardingViewModel(
                    userRepository!!,
                    settingsRepository!!
                ) as T
            }
            modelClass.isAssignableFrom(DatabaseInspectorViewModel::class.java) -> {
                DatabaseInspectorViewModel(
                    userRepository!!,
                    aiRepository!!
                ) as T
            }
            modelClass.isAssignableFrom(AiSyncViewModel::class.java) -> {
                AiSyncViewModel(
                    aiRepository!!,
                    userRepository!!,
                    WorkManager.getInstance(application),
                    application
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
