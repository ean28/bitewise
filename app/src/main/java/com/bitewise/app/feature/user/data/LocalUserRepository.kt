package com.bitewise.app.feature.user.data

import com.bitewise.app.feature.user.data.UserProfileDAO
import com.bitewise.app.feature.user.data.local.UserEntity
import com.bitewise.app.domain.user.models.UserContext
import com.bitewise.app.domain.user.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalUserRepository(private val userProfileDAO: UserProfileDAO) : UserRepository {
    override fun getUserContext(): Flow<UserContext?> {
        return userProfileDAO.getUserProfile().map { entity ->
            entity?.let {
                UserContext(
                    age = it.age,
                    weight = it.weight,
                    height = it.height,
                    activity = it.activity.trim(),
                    conditions = it.conditions.split(",")
                        .map { s -> s.trim() }
                        .filter { s -> s.isNotBlank() && !s.equals("None", ignoreCase = true) },
                    dietary = it.dietary.split(",")
                        .map { s -> s.trim() }
                        .filter { s -> s.isNotBlank() && !s.equals("None", ignoreCase = true) },
                    allergies = it.allergies.split(",")
                        .map { s -> s.trim() }
                        .filter { s -> s.isNotBlank() && !s.equals("None", ignoreCase = true) }
                )
            }
        }
    }

    override suspend fun saveUserContext(context: UserContext) {
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
