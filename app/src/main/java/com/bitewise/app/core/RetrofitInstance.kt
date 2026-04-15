package com.bitewise.app.core

import com.bitewise.app.BuildConfig
import com.bitewise.app.feature.product.data.OpenFoodFactsApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory

object RetrofitInstance {

    private val json: Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }
    
    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    private val contentType = "application/json".toMediaType()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    val api: OpenFoodFactsApi by lazy {
        retrofit.create(OpenFoodFactsApi::class.java)
    }
}
