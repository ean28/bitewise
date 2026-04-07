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
    fun verifyFullDatabaseMapping() = runBlocking {

        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val db = LocalProductDatabaseModule.getDatabase(context)
        val dao = db.productDao()

        val entities = dao.fetchSearchProductItems()

        if (entities.isEmpty()) {
            Log.e("BITEWISE_TEST", "❌ No products found in DB")
            return@runBlocking
        }

        entities.take(10).forEachIndexed { index, entity -> // limit

            val domain = entity.toDomain()

            Log.d("BITEWISE_TEST", "=====================================")

            // RAW ENTITY
            Log.d("BITEWISE_TEST", "RAW ENTITY ($index):")
            Log.d("BITEWISE_TEST", "code: ${entity.code}")
            Log.d("BITEWISE_TEST", "name: ${entity.productName}")
            Log.d("BITEWISE_TEST", "brands: ${entity.brands}")
            Log.d("BITEWISE_TEST", "image: ${entity.imageFrontUrl}")
            Log.d("BITEWISE_TEST", "nutriscore: ${entity.nutriscoreGrade}")
            Log.d("BITEWISE_TEST", "nova: ${entity.novaGroup}")
            Log.d("BITEWISE_TEST", "ecoscore: ${entity.ecoScoreGrade}")
            Log.d("BITEWISE_TEST", "categories: ${entity.categoriesTags}")
            Log.d("BITEWISE_TEST", "allergens: ${entity.allergenTags}")
            Log.d("BITEWISE_TEST", "labels: ${entity.labels}")
            Log.d("BITEWISE_TEST", "ingredients_text: ${entity.ingredientsText}")
            Log.d("BITEWISE_TEST", "nutriments_json: ${entity.nutriments}")
            Log.d("BITEWISE_TEST", "=====================================")

            // DOMAIN VALUES
            Log.d("BITEWISE_TEST", "--- DOMAIN MODEL ---")
            Log.d("BITEWISE_TEST", "name: ${domain.name}")

            // Nutrients
            val nutrients = domain.productNutriments
            if (nutrients.isNullOrEmpty()) {
                Log.d("BITEWISE_TEST", "Nutrients: NONE")
            } else {
                nutrients.forEach {
                    Log.d("BITEWISE_TEST", "Nutrient -> ${it.name}: ${it.amount} ${it.unit}")
                }
            }

            val ingredients = domain.productIngredients
            if (ingredients.isEmpty()) {
                Log.d("BITEWISE_TEST", "Ingredients: NONE")
            } else {
                ingredients.forEach {
                    Log.d("BITEWISE_TEST", "Ingredient -> ${it.name}")
                    if (it.subIngredients.isNotEmpty()) {
                        Log.d("BITEWISE_TEST", "   Sub: ${it.subIngredients}")
                    }
                    Log.d("BITEWISE_TEST", "   Risky: ${it.isRisky}")
                }
            }

            // Scores
            val scores = domain.productScores
            Log.d("BITEWISE_TEST", "Scores:")
            Log.d("BITEWISE_TEST", "   NutriScore: ${scores?.nutritionGrade}")
            Log.d("BITEWISE_TEST", "   Nova: ${scores?.novaGroup}")
            Log.d("BITEWISE_TEST", "   EcoScore: ${scores?.ecoScoreGrade}")

            Log.d("BITEWISE_TEST", "=====================================")
        }
    }
}