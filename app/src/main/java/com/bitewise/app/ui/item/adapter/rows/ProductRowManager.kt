package com.bitewise.app.ui.item.adapter.rows

import com.bitewise.app.domain.Product

sealed class ProductRowManager {
    data class Header(val product: Product) : ProductRowManager()
    data class FirstContainer(val product: Product) : ProductRowManager()
    data class SecondContainer(val product: Product) : ProductRowManager()
}
