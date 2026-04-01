package com.bitewise.app.data.local

import android.content.Context

class RecentProductHistory(context: Context) {
    private val prefs = context.getSharedPreferences("recent_products_prefs", Context.MODE_PRIVATE)

    fun addBarcode(barcode: String) {
        val current = getBarcodes().toMutableList()
        current.remove(barcode)
        current.add(0, barcode)
        
        val limited = current.take(10)
        prefs.edit().putString("recent_barcodes", limited.joinToString(",")).apply()
    }

    fun getBarcodes(): List<String> {
        val saved = prefs.getString("recent_barcodes", "") ?: ""
        return if (saved.isEmpty()) emptyList() else saved.split(",")
    }
}
