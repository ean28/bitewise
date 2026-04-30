package com.bitewise.app.feature.search

import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bitewise.app.feature.product.api.Product
import com.bitewise.app.feature.product.api.ProductRepository
import com.bitewise.app.core.UiState
import com.bitewise.app.core.ui.GradeManager
import com.bitewise.app.feature.ai.api.AiRepository
import com.bitewise.app.feature.ai.data.local.AiAnalysisEntity
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

data class SearchTileUiState(
    val product: Product,
    val code: String,
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

    @OptIn(FlowPreview::class)
    fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collectLatest { query ->
                    performSearch(query)
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun fetchAllProducts() {
        onSearchQueryChanged("")
    }

    private suspend fun performSearch(query: String) {
        _searchState.value = UiState.Loading
        try {
            val products = if (query.isBlank()) {
                repository.getAllProducts()
            } else {
                repository.searchProducts(query)
            }

            val analyses = aiRepository.getAllAnalyses().first()
            val analysisMap = analyses.associateBy { it.barcode }
            val uiStates = products.map {
                it.toUiState(analysisMap[it.code])
            }
            _searchState.value = UiState.Success(uiStates)

        } catch (e: Exception) {
            if (e is CancellationException) throw e
            _searchState.value = UiState.Error(e.message ?: "Something went wrong")

            Log.e("SearchViewModel", "Error during search", e)
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
            ecoGradeRes = GradeManager.getEcoDrawable(productScores?.ecoScoreGrade),
            aiScore = analysis?.score?.toInt()?.toString(),
            showAiScore = analysis != null
        )
    }
}
