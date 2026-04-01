package com.bitewise.app.data.local

import androidx.room.Dao
import androidx.room.Query
import com.bitewise.app.data.local.entities.ProductEntity

@Dao
interface ProductDAO {
    @Query("SELECT * FROM products WHERE search_text Like '%' || :query ||  '%' LIMIT 50")
    suspend fun searchProducts(query: String): List<ProductEntity>

    @Query("SELECT * FROM products LIMIT 18")
    suspend fun  fetchSearchProductItems(): List<ProductEntity>

    @Query("SELECT * FROM products WHERE code = :barcode LIMIT 1")
    suspend fun searchByBarcode(barcode: String): ProductEntity?
}
