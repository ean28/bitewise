package com.bitewise.app.feature.user.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bitewise.app.feature.ai.data.AiAnalysisDAO
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.feature.ai.data.local.AiSyncLog
import com.bitewise.app.feature.ai.data.AiSyncLogDAO
import com.bitewise.app.feature.user.data.local.UserEntity

@Database(entities = [UserEntity::class, AiAnalysisEntity::class, AiSyncLog::class], version = 6, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDAO
    abstract fun aiAnalysisDao(): AiAnalysisDAO
    abstract fun aiSyncLogDao(): AiSyncLogDAO

    companion object {
        @Volatile
        private var INSTANCE: UserDatabase? = null

        fun getDatabase(context: Context): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDatabase::class.java,
                    "user_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
