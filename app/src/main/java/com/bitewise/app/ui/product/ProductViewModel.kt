package com.bitewise.app.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.domain.Product
import com.bitewise.app.data.repository.LocalProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: LocalProductRepository
) : ViewModel() {

    // VM uses local database only temporarily with no API calls
    // TODO("add api calls to update database")

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            val result = repository.getProductByBarcode(barcode)
            _product.value = result
        }
    }

    // Search products locally
    fun search(query: String) {
        viewModelScope.launch {
            val results = repository.searchProducts(query)
            _searchResults.value = results
        }
    }

    // TODO ("add handler to switch database when user chooses from country selector")
    fun fetchAllProducts() {
        viewModelScope.launch {
            val results = repository.getAllProducts()
            _searchResults.value = results
        }
    }
}