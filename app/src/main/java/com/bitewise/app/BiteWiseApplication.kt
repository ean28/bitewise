package com.bitewise.app

import android.app.Application
import com.bitewise.app.data.local.di.LocalProductDatabaseModule
import com.bitewise.app.data.repository.LocalProductRepository
import com.bitewise.app.domain.ProductRepository

class BiteWiseApplication : Application() {

    private val database by lazy { LocalProductDatabaseModule.getDatabase(this) }
    val productRepository: ProductRepository by lazy { 
        LocalProductRepository(database.productDao()) 
    }

    override fun onCreate() {
        super.onCreate()
    }
}
