package com.bitewise.app.data.network

import com.bitewise.app.data.network.model.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface OpenFoodFactsApi {

    @GET("product/{barcode}")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String =
            "code,product_name,brands,image_front_url,nutriments,nutrition_grades,allergens,serving_size"
    ): Response<ProductResponse>
}