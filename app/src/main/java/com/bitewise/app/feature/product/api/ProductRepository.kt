package com.bitewise.app.feature.product.api

import com.bitewise.app.feature.product.api.Product

interface ProductRepository {
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun getAllProducts(): List<Product>
    suspend fun getScannableProducts(limit: Int, excludedBarcodes: List<String> = emptyList()): List<Product>
}
