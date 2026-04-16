package com.bitewise.app.feature.ai.data
import com.bitewise.app.feature.ai.data.local.AiSyncLog


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AiSyncLogDAO {
    @Insert
    suspend fun insertLog(log: AiSyncLog)

    @Query("SELECT * FROM ai_sync_logs ORDER BY timestamp DESC LIMIT 5")
    fun getRecentLogs(): Flow<List<AiSyncLog>>
}
