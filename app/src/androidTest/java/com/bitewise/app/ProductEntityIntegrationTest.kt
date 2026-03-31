package com.bitewise.app

import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bitewise.app.data.local.di.LocalProductDatabaseModule
import com.bitewise.app.data.mapper.toDomain
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductEntityIntegrationTest {

    @Test
    fun verifyRealDatabaseMapping()  = runBlocking {
        // 1. Call database from asset
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = LocalProductDatabaseModule.getDatabase(context)
        val dao = db.productDao()

        // 2. Fetch some products .db file
        val entities = dao.fetchSearchProductItems()

        entities.forEach { entity ->
            // 3. Map to Domain
            val domainProduct = entity.toDomain()

            // 4. "Console" output via Logcat
            Log.d("BITEWISE_TEST", "=====================================")
            Log.d("BITEWISE_TEST", "Product: ${domainProduct.name}")

            val nutrients = domainProduct.productNutriments
            if (nutrients.isNullOrEmpty()) {
                Log.d("BITEWISE_TEST", "Nutrients: NONE FOUND")
            } else {
                nutrients.forEach { n ->
                    Log.d("BITEWISE_TEST", "-> ${n.name}: ${n.amount} ${n.unit}")
                }
            }
        }
    }
}