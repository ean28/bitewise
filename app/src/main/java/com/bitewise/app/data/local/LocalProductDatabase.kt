package com.bitewise.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bitewise.app.data.local.converter.NutrimentsConverter
import com.bitewise.app.data.local.entities.ProductEntity

// renamed database name to state its purpose
@Database(entities = [ProductEntity::class], version = 2, exportSchema = false)
@TypeConverters(NutrimentsConverter::class)
abstract class LocalProductDatabase : RoomDatabase() {
    abstract fun productDao() : ProductDAO
}
