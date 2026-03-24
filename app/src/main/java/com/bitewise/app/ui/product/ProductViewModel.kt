package com.bitewise.app.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.model.Product
import com.bitewise.app.data.repository.LocalProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: LocalProductRepository
) : ViewModel() {

    // ✅ Single product (Home / Details)
    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product

    // ✅ Search results (NON-null list)
    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> = _searchResults

    // ✅ Fetch single product (barcode)
    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            val result = repository.getProductByBarcode(barcode)
            _product.value = result
        }
    }

    // ✅ Search products locally
    fun search(query: String) {
        viewModelScope.launch {
            val results = repository.searchProducts(query)
            _searchResults.value = results
        }
    }

    fun fetchAllProducts() {
        viewModelScope.launch {
            val results = repository.getAllProducts()
            _searchResults.value = results
        }
    }
}