package com.bitewise.app.data.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bitewise.app.data.local.LocalProductDatabase
import com.bitewise.app.data.local.converter.NutrimentsConverter

@TypeConverters(NutrimentsConverter::class)
object LocalProductDatabaseModule {
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
                    PRIMARY KEY(`code`)
                )
            """.trimIndent())
            db.execSQL("INSERT INTO products_new SELECT * FROM products")
            db.execSQL("DROP TABLE products")
            db.execSQL("ALTER TABLE products_new RENAME TO products")
        }
    }

    fun getDatabase(context: Context): LocalProductDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context,
                LocalProductDatabase::class.java,
                "philippines_products_cleaned_FINAL.db"
            )
                .createFromAsset("databases/philippines_products_cleaned_FINAL.db")
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build().also { instance = it }
        }
    }
}
