package com.bitewise.app.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.core.UiState
import com.bitewise.app.core.ui.GradeManager
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SearchTileUiState(
    val product: Product,
    val code: String? = null,
    val name: String,
    val imageUrl: String?,
    val nutriGradeRes: Int,
    val novaGradeRes: Int,
    val ecoGradeRes: Int,
    val aiScore: String? = null,
    val showAiScore: Boolean = false
)

class SearchViewModel(
    private val repository: ProductRepository,
    private val aiRepository: AiRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    
    private val _searchState = MutableStateFlow<UiState<List<SearchTileUiState>>>(UiState.Loading)
    val searchState: StateFlow<UiState<List<SearchTileUiState>>> = _searchState.asStateFlow()

    init {
        observeSearchQuery()
    }

    fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery.collectLatest { query ->
                if (query.isNotBlank()) {
                    delay(300)
                }
                performSearch(query)
            }
        }
    }

    fun fetchAllProducts() {
        viewModelScope.launch {
            performSearch("")
        }
    }

    private suspend fun performSearch(query: String) {
        _searchState.value = UiState.Loading
        try {
            val products = if (query.isBlank()) {
                repository.getAllProducts()
            } else {
                repository.searchProducts(query)
            }
            
            aiRepository.getAllAnalyses().collect { analyses ->
                val analysisMap = analyses.associateBy { it.barcode }
                val uiStates = products.map { product ->
                    product.toUiState(analysisMap[product.code])
                }
                _searchState.value = UiState.Success(uiStates)
            }
        } catch (e: Exception) {
            _searchState.value = UiState.Error(e.message ?: "Search failed")
        }
    }

    private fun Product.toUiState(analysis: AiAnalysisEntity? = null): SearchTileUiState {
        return SearchTileUiState(
            product = this,
            code = this.code,
            name = name,
            imageUrl = imageUrl,
            nutriGradeRes = GradeManager.getNutriDrawable(productScores?.nutritionGrade),
            novaGradeRes = GradeManager.getNovaDrawable(productScores?.novaGroup),
            ecoGradeRes = GradeManager.getEcoDrawable(null),
            aiScore = analysis?.score?.toInt()?.toString(),
            showAiScore = analysis != null
        )
    }
}
