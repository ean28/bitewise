package com.bitewise.app.data.network.repository

import android.util.Log
import com.bitewise.app.data.network.OpenFoodFactsApi
import com.bitewise.app.data.network.model.Product
import com.bitewise.app.data.network.model.ProductResponse
import okhttp3.logging.LoggingEventListener
import retrofit2.Response

class ProductRepository(
    private val api: OpenFoodFactsApi) {

    suspend fun getProductByBarcode(barcode: String): Product? {
        return try{
            val response: Response<ProductResponse> = api.getProductByBarcode(barcode)
            Log.d("API_TEST", "Raw response: ${response.body()}")
            if (response.isSuccessful && response.body()?.status == 1) {
                response.body()?.product
            } else {
                Log.d("API_TEST", "Product not found or API ERROR: ${response.body()}")
                null
            }
        } catch(e: Exception) {
            Log.d("API_TEST", "Error fetching product:", e)
            e.printStackTrace()
            null
        }
    }
}