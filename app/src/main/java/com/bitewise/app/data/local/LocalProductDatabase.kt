package com.bitewise.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bitewise.app.data.local.entities.ProductEntity

// renamed database name to state its purpose
@Database(entities = [ProductEntity::class], version = 2, exportSchema = false)
abstract class LocalProductDatabase : RoomDatabase() {
    abstract fun productDao() : ProductDAO
}
