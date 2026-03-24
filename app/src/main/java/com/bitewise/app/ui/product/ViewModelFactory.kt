package com.bitewise.app.ui.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bitewise.app.data.repository.LocalProductRepository

class ProductViewModelFactory(private val repository: LocalProductRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProductViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}