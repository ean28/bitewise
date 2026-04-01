package com.bitewise.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.data.local.RecentProductHistory
import com.bitewise.app.domain.ProductRepository
import com.bitewise.app.domain.models.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: ProductRepository,
    private val history: RecentProductHistory
) : ViewModel() {

    private val _recentProducts = MutableStateFlow<List<Product>>(emptyList())
    val recentProducts: StateFlow<List<Product>> = _recentProducts

    fun loadRecentItems() {
        viewModelScope.launch {
            val barcodes = history.getBarcodes()
            val products = barcodes.mapNotNull { code ->
                repository.getProductByBarcode(code)
            }
            _recentProducts.value = products
        }
    }
}