package com.bitewise.app.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.core.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow<UiState<List<Product>>>(UiState.Loading)
    val searchState: StateFlow<UiState<List<Product>>> = _searchState

    fun search(query: String) {
        viewModelScope.launch {
            _searchState.value = UiState.Loading
            try {
                val results = repository.searchProducts(query)
                _searchState.value = UiState.Success(results)
            } catch (e: Exception) {
                _searchState.value = UiState.Error(e.message ?: "Unknown search error")
            }
        }
    }

    fun fetchAllProducts() {
        viewModelScope.launch {
            _searchState.value = UiState.Loading
            try {
                val results = repository.getAllProducts()
                _searchState.value = UiState.Success(results)
            } catch (e: Exception) {
                _searchState.value = UiState.Error(e.message ?: "Unknown fetch error")
            }
        }
    }
}
