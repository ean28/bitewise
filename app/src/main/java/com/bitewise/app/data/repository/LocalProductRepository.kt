package com.bitewise.app.data.repository

import android.util.Log
import com.bitewise.app.data.local.ProductDAO
import com.bitewise.app.data.mapper.toDomain
import com.bitewise.app.domain.Product
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalProductRepository(
    private val dao: ProductDAO,
    //using Dispatchers.IO since this is a (offline) network output task
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    companion object {
        private const val REPO_TAG = "🐓REPO-DATABASE"
    }
    suspend fun searchProducts(query: String): List<Product> {
        return withContext(defaultDispatcher) {
            Log.d(REPO_TAG, "Querying: $query in local DB")
            val results = dao.searchProducts(query)

            Log.d(REPO_TAG, "Found ${results.size} items for \"$query\"")
            results.map { it.toDomain() }
        }
    }

    suspend fun getProductByBarcode(barcode: String): Product? {
        return withContext(defaultDispatcher) {
            Log.d(REPO_TAG, "Fetching item with barcode $barcode in local DB")
            val result = dao.searchByBarcode(barcode)

            if (result != null) {
                Log.d(REPO_TAG, "Found product: ${result.productName}")
            } else {
                Log.d(REPO_TAG, "No local match found")
            }
            result?.toDomain()
        }
    }

    suspend fun getAllProducts(): List<Product> {
        return withContext(defaultDispatcher) {
            val results = dao.fetchSearchProductItems()
            Log.d(REPO_TAG, "Fetched ${results.size} items")

            results.map { it.toDomain() }
        }
    }

    //TODO("Create another function for category search")
}