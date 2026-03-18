package com.bitewise.app.ui.product

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.data.network.model.Product
import com.bitewise.app.data.network.repository.ProductRepository
import kotlinx.coroutines.launch

class ProductViewModel(private val repository: ProductRepository): ViewModel() {

    private val _product = MutableLiveData<Product?>()
    val product: LiveData<Product?> get() = _product

    fun fetchProduct(barcode: String) {
        viewModelScope.launch {
            val result = repository.getProductByBarcode(barcode)
            _product.postValue(result)
        }
    }
}