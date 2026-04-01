package com.bitewise.app.domain

import com.bitewise.app.domain.models.Product

interface ProductRepository {
    suspend fun searchProducts(query: String): List<Product>
    suspend fun getProductByBarcode(barcode: String): Product?
    suspend fun getAllProducts(): List<Product>
}
