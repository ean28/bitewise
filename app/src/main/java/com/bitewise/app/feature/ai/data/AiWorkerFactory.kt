package com.bitewise.app.feature.ai.data

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.bitewise.app.feature.ai.data.GeminiService
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.domain.HealthScoringEngine
import com.bitewise.app.domain.user.repository.UserRepository

class AiWorkerFactory(
    private val aiRepository: AiRepository,
    private val userRepository: UserRepository,
    private val geminiService: GeminiService,
    private val healthScoringEngine: HealthScoringEngine
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            AiBatchWorker::class.java.name ->
                AiBatchWorker(
                    appContext, 
                    workerParameters, 
                    aiRepository, 
                    userRepository, 
                    geminiService,
                    healthScoringEngine
                )
            else -> null
        }
    }
}
