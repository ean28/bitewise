package com.bitewise.app.domain.user.repository

import com.bitewise.app.domain.user.models.UserContext
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserContext(): Flow<UserContext?>
    suspend fun saveUserContext(context: UserContext)
}
