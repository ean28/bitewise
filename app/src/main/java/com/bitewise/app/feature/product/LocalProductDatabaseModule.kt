package com.bitewise.app.feature.product

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bitewise.app.feature.product.data.LocalProductDatabase
import com.bitewise.app.feature.product.data.local.NutrimentsConverter
import java.util.concurrent.Executors

object LocalProductDatabaseModule {
    private const val TAG = "DB_DEBUG"
    @Volatile
    private var instance: LocalProductDatabase? = null

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `products_new` (
                    `code` TEXT NOT NULL, 
                    `product_name` TEXT, 
                    `brands` TEXT, 
                    `image_front_url` TEXT, 
                    `nutriscore_grade` TEXT, 
                    `nova_group` INTEGER, 
                    `categories_tags` TEXT, 
                    `search_text` TEXT, 
                    `allergens_tags` TEXT,
                    `nutriments_json` TEXT,
                    
                    `ecoscore_grade` TEXT,
                    `ingredients_text` TEXT,
                    `labels` TEXT,
                   
                    PRIMARY KEY(`code`)
                )
            """.trimIndent())
            // This prevents crashes if v1 and v2 have different column counts.
            db.execSQL("""
                INSERT INTO `products_new` (
                    `code`, `product_name`, `brands`, `image_front_url`, 
                    `nutriscore_grade`, `nova_group`, `categories_tags`, 
                    `search_text`, `allergens_tags`, `nutriments_json`
                )
                SELECT 
                    `code`, `product_name`, `brands`, `image_front_url`, 
                    `nutriscore_grade`, `nova_group`, `categories_tags`, 
                    `search_text`, `allergens_tags`, `nutriments_json` 
                FROM `products`
            """.trimIndent())

            db.execSQL("DROP TABLE products")
            db.execSQL("ALTER TABLE products_new RENAME TO products")
        }
    }

    fun getDatabase(context: Context): LocalProductDatabase {
        return instance ?: synchronized(this) {
            instance ?:  try {
                Room.databaseBuilder(
                    context.applicationContext,
                    LocalProductDatabase::class.java,
                    "philippines_products.db"
                )
                    .createFromAsset("databases/philippines_products.db")
                    .addMigrations(MIGRATION_1_2)
                    .setQueryCallback({ sqlQuery, bindArgs ->
                        Log.d(TAG, "SQL Query: $sqlQuery SQL Args: $bindArgs")
                    }, Executors.newSingleThreadExecutor())
                    .fallbackToDestructiveMigration()
                    .build().also { instance = it }
            } catch (e: Exception) {
                Log.e(TAG, "Database initialization failed!", e)
                throw e
            }
        }
    }
}
