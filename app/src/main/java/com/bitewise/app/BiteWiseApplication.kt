package com.bitewise.app

import android.app.Application
import androidx.work.Configuration
import com.bitewise.app.feature.ai.data.AiWorkerFactory
import com.bitewise.app.feature.user.data.UserDatabase
import com.bitewise.app.feature.product.LocalProductDatabaseModule
import com.bitewise.app.feature.ai.data.LocalAiRepository
import com.bitewise.app.feature.user.data.LocalUserRepository
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.feature.ai.data.GeminiService
import com.bitewise.app.feature.ai.data.AiConfiguration
import com.bitewise.app.feature.product.data.LocalRecentProductRepository
import com.bitewise.app.feature.product.data.LocalProductRepository
import com.bitewise.app.feature.settings.data.LocalSettingsRepository
import com.bitewise.app.feature.product.api.RecentProductRepository
import com.bitewise.app.domain.user.repository.UserRepository
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.domain.HealthScoringEngine
import com.bitewise.app.feature.ai.AiPayloadProjector
import com.bitewise.app.domain.settings.SettingsRepository
import com.bitewise.app.feature.ai.data.AiCommunicationSchema

class BiteWiseApplication : Application(), Configuration.Provider {

    // Manual Dependency Injection
    private val productDatabase by lazy { LocalProductDatabaseModule.getDatabase(this) }
    private val userDatabase by lazy { UserDatabase.getDatabase(this) }

    val productRepository: ProductRepository by lazy { 
        LocalProductRepository(productDatabase.productDao()) 
    }
    
    val userRepository: UserRepository by lazy {
        LocalUserRepository(userDatabase.userProfileDao())
    }

    val aiRepository: AiRepository by lazy {
        LocalAiRepository(
            context = this,
            productDao = productDatabase.productDao(),
            analysisDao = userDatabase.aiAnalysisDao(),
            syncLogDao = userDatabase.aiSyncLogDao()
        )
    }

    private val aiPayloadProjector by lazy { AiPayloadProjector() }

    val healthScoringEngine: HealthScoringEngine by lazy {
        HealthScoringEngine(productRepository, userRepository, aiRepository, aiPayloadProjector)
    }

    val recentProductRepository: RecentProductRepository by lazy { 
        LocalRecentProductRepository(this)
    }

    val settingsRepository: SettingsRepository by lazy {
        LocalSettingsRepository(this)
    }

    override val workManagerConfiguration: Configuration
        get() {
            // Centralized instructions from the Schema object
            val instructions = AiCommunicationSchema.SYSTEM_PROMPT

            val geminiService = GeminiService(instructions)

            val factory = AiWorkerFactory(
                aiRepository = aiRepository,
                userRepository = userRepository,
                geminiService = geminiService,
                healthScoringEngine = healthScoringEngine
            )

            return Configuration.Builder()
                .setWorkerFactory(factory)
                .setMinimumLoggingLevel(android.util.Log.INFO)
                .build()
        }
}
