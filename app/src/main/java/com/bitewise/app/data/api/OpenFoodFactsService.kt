package com.bitewise.app.data.api

import com.bitewise.app.data.api.constants.ApiConstants
import com.bitewise.app.data.api.model.ProductResponse
import retrofit2.Response
import retrofit2.http.*

interface OpenFoodFactsApi {

    @GET("product/{barcode}")
    suspend fun getProductByBarcode(
        @Path("barcode") barcode: String,
        @Query("fields") fields: String = ApiConstants.SEARCH_FIELDS
    ): Response<ProductResponse>
}
