package com.bitewise.app.data.repository

import com.bitewise.app.data.local.ProductDAO
import com.bitewise.app.data.mapper.toDomain
import com.bitewise.app.model.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalProductRepository(
    private val dao: ProductDAO
) {

    suspend fun searchProducts(query: String): List<Product> {
        return withContext(Dispatchers.IO) {
            dao.searchProducts(query).map { it.toDomain() }
        }
    }

    suspend fun getProductByBarcode(barcode: String): Product? {
        return withContext(Dispatchers.IO) {
            dao.searchByBarcode(barcode)?.toDomain()
        }
    }

    suspend fun getAllProducts(): List<Product> {
        return withContext(Dispatchers.IO) {
            dao.fetchSearchProductItems().map { it.toDomain() }
        }
    }

    //TODO("Create another suspend function for barcode search)
}