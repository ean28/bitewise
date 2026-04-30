package com.bitewise.app.feature.product.api

import com.bitewise.app.feature.product.api.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun getAllProducts(): List<Product>
    
     // TODO: Add support for filtering/limiting in the future.
    fun getScannableProductsFlow(): Flow<List<Product>>
}
