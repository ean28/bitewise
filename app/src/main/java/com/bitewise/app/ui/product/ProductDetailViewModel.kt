package com.bitewise.app.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.data.local.RecentProductHistory
import com.bitewise.app.domain.models.Product
import com.bitewise.app.domain.ProductRepository
import com.bitewise.app.ui.common.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val history: RecentProductHistory
) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<Product>>(UiState.Loading)
    val productState: StateFlow<UiState<Product>> = _productState

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            _productState.value = UiState.Loading
            try {
                history.addBarcode(barcode)
                val result = repository.getProductByBarcode(barcode)
                if (result != null) {
                    _productState.value = UiState.Success(result)
                } else {
                    _productState.value = UiState.Error("Product not found")
                }
            } catch (e: Exception) {
                _productState.value = UiState.Error(e.message ?: "Unknown fetch error")
            }
        }
    }
}
