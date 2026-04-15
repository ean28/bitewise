package com.bitewise.app.feature.product.data

import androidx.room.Dao
import androidx.room.Query
import com.bitewise.app.feature.product.data.local.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDAO {
    @Query("SELECT * FROM products WHERE search_text Like '%' || :query ||  '%' LIMIT 50")
    suspend fun searchProducts(query: String): List<ProductEntity>

    @Query("SELECT * FROM products WHERE code = :barcode LIMIT 1")
    suspend fun searchByBarcode(barcode: String): ProductEntity?

    @Query("SELECT * FROM products")
    suspend fun fetchSearchProductItems(): List<ProductEntity>

    /**
     * Fetches products that meet the criteria for AI analysis.
     * Filtering against already analyzed products is now handled in the Repository.
     */
    @Query("""
        SELECT * FROM products
        WHERE (product_name IS NOT NULL AND product_name != '')
            AND (nutriments_json IS NOT NULL AND nutriments_json != '{}')
            AND (ingredients_text IS NOT NULL AND ingredients_text != '')
            AND (nutriscore_grade IS NOT NULL AND nutriscore_grade != 'unknown' AND nutriscore_grade != 'not-applicable')
            AND (nova_group IS NOT NULL AND nova_group != '')
            AND code > :lastBarcode
        ORDER BY code ASC
        LIMIT :limit
    """)
    suspend fun getNextScannableProducts(lastBarcode: String, limit: Int): List<ProductEntity>

    @Query("""
        SELECT * FROM products
        WHERE (product_name IS NOT NULL AND product_name != '')
            AND (nutriments_json IS NOT NULL AND nutriments_json != '{}')
            AND (ingredients_text IS NOT NULL AND ingredients_text != '')
            AND (nutriscore_grade IS NOT NULL AND nutriscore_grade != 'unknown' AND nutriscore_grade != 'not-applicable')
            AND (nova_group IS NOT NULL AND nova_group != '')
            AND code NOT IN (:excludedBarcodes)
        LIMIT :limit
    """)
    suspend fun getScannableProducts(limit: Int, excludedBarcodes: List<String>): List<ProductEntity>

    @Query("""
        SELECT COUNT(*) FROM products
        WHERE (product_name IS NOT NULL AND product_name != '')
            AND (nutriments_json IS NOT NULL AND nutriments_json != '{}')
            AND (ingredients_text IS NOT NULL AND ingredients_text != '')
            AND (nutriscore_grade IS NOT NULL AND nutriscore_grade != 'unknown' AND nutriscore_grade != 'not-applicable')
            AND (nova_group IS NOT NULL AND nova_group != '')
    """)
    suspend fun getTotalAiScannableCount(): Int

    @Query("SELECT COUNT(*) FROM products WHERE search_text LIKE '%' || :query || '%'")
    fun getScannableCountFlow(query: String): Flow<Int>
}
