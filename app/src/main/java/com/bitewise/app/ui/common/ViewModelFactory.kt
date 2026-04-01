package com.bitewise.app.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bitewise.app.data.local.RecentProductHistory
import com.bitewise.app.domain.ProductRepository
import com.bitewise.app.ui.home.HomeViewModel
import com.bitewise.app.ui.product.ProductDetailViewModel
import com.bitewise.app.ui.search.SearchViewModel

class ViewModelFactory(
    private val repository: ProductRepository,
    private val history: RecentProductHistory
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProductDetailViewModel::class.java) -> {
                ProductDetailViewModel(repository, history) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository, history) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
