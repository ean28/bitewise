package com.bitewise.app.feature.product.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bitewise.app.feature.product.data.local.NutrimentsConverter
import com.bitewise.app.feature.product.data.local.ProductEntity

@Database(entities = [ProductEntity::class], version = 2, exportSchema = false)
@TypeConverters(NutrimentsConverter::class)
abstract class LocalProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO
}
