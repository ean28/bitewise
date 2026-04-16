package com.bitewise.app.feature.product.data

import android.content.Context
import androidx.core.content.edit
import com.bitewise.app.feature.product.api.RecentProductRepository

class LocalRecentProductRepository(context: Context) : RecentProductRepository {
    private val prefs = context.getSharedPreferences("recent_products_prefs", Context.MODE_PRIVATE)

    override fun addBarcode(barcode: String) {
        val current = getBarcodes().toMutableList()
        current.remove(barcode)
        current.add(0, barcode)
        
        val limited = current.take(10)
        prefs.edit { putString("recent_barcodes", limited.joinToString(",")) }
    }

    override fun getBarcodes(): List<String> {
        val saved = prefs.getString("recent_barcodes", "") ?: ""
        return if (saved.isEmpty()) emptyList() else saved.split(",")
    }
}
