package com.bitewise.app.feature.product.api

interface RecentProductRepository {
    fun addBarcode(barcode: String)
    fun getBarcodes(): List<String>
}
