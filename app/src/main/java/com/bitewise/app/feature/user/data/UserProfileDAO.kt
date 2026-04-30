package com.bitewise.app.feature.user.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bitewise.app.feature.user.data.local.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserProfileDAO {
    @Query("SELECT * FROM user_profile WHERE id = 0")
    fun getUserProfile(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(user: UserEntity)
}
