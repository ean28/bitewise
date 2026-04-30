package com.bitewise.app.feature.user.data

import com.bitewise.app.feature.user.data.local.UserEntity
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalUserRepository(
    private val userProfileDAO: UserProfileDAO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserRepository {
    override fun getUserContext(): Flow<UserContext?> {
        return userProfileDAO.getUserProfile().map { entity ->
            entity?.let {
                UserContext(
                    age = it.age,
                    weight = it.weight,
                    height = it.height,
                    activity = it.activity.trim(),
                    conditions = parseStringList(it.conditions),
                    dietary = parseStringList(it.dietary),
                    allergies = parseStringList(it.allergies)
                )
            }
        }.flowOn(defaultDispatcher)
    }

    private fun parseStringList(raw: String): List<String> {
        return raw.split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() && !it.equals("None", ignoreCase = true) }
            .map { if (it.contains("|")) it.split("|")[1] else it }
    }

    override suspend fun saveUserContext(context: UserContext) {
        withContext(defaultDispatcher) {
            userProfileDAO.saveProfile(
                UserEntity(
                    age = context.age,
                    weight = context.weight,
                    height = context.height,
                    activity = context.activity.trim(),
                    conditions = context.conditions.joinToString(","),
                    dietary = context.dietary.joinToString(","),
                    allergies = context.allergies.joinToString(",")
                )
            )
        }
    }
}
