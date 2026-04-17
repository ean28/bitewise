package com.bitewise.app.feature.product.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.feature.product.api.RecentProductRepository
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import com.bitewise.app.core.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val repository: ProductRepository,
    private val history: RecentProductRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _productState = MutableStateFlow<UiState<Pair<Product, AiAnalysisEntity?>>>(UiState.Loading)
    val productState: StateFlow<UiState<Pair<Product, AiAnalysisEntity?>>> = _productState

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            if (_productState.value is UiState.Loading) return@launch

            try {
                history.addBarcode(barcode)
                val product = repository.getProductByBarcode(barcode)
                val analysis = aiRepository.getExistingAnalysis(barcode)

                if (product != null) {
                    _productState.value = UiState.Success(product to analysis)
                } else {
                    _productState.value = UiState.Error("Product not found")
                }
            } catch (e: Exception) {
                _productState.value = UiState.Error(e.message ?: "Unknown fetch error")
            }
        }
    }
}
