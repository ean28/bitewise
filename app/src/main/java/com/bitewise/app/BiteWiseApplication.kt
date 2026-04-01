package com.bitewise.app

import android.app.Application
import com.bitewise.app.data.local.RecentProductHistory
import com.bitewise.app.data.local.di.LocalProductDatabaseModule
import com.bitewise.app.data.repository.LocalProductRepository
import com.bitewise.app.domain.ProductRepository

class BiteWiseApplication : Application() {

    // Manual Dependency Injection
    private val database by lazy { LocalProductDatabaseModule.getDatabase(this) }
    
    val productRepository: ProductRepository by lazy { 
        LocalProductRepository(database.productDao()) 
    }
    
    val recentHistory by lazy { 
        RecentProductHistory(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}
