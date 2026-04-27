package com.bitewise.app.feature.product.data

import android.content.Context
import androidx.core.content.edit
import com.bitewise.app.core.Constants
import com.bitewise.app.feature.product.api.RecentProductRepository

class LocalRecentProductRepository(context: Context) : RecentProductRepository {
    private val prefs = context.getSharedPreferences(Constants.PREFS_RECENT_PRODUCTS, Context.MODE_PRIVATE)

    override fun addBarcode(barcode: String) {
        val current = getBarcodes().toMutableList()
        current.remove(barcode)
        current.add(0, barcode)
        
        val limited = current.take(Constants.MAX_RECENT_ITEMS)
        prefs.edit { putString(Constants.KEY_RECENT_BARCODES, limited.joinToString(",")) }
    }

    override fun getBarcodes(): List<String> {
        val saved = prefs.getString(Constants.KEY_RECENT_BARCODES, "") ?: ""
        return if (saved.isEmpty()) emptyList() else saved.split(",")
    }
}
