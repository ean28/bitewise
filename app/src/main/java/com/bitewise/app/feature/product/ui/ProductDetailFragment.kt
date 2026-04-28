package com.bitewise.app.feature.product.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.databinding.FragmentProductInformationBinding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.Constants
import com.bitewise.app.core.UiState
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.feature.product.ui.adapter.ProductInformationAdapter
import com.bitewise.app.feature.product.ui.adapter.rows.ProductRowManager
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class ProductDetailFragment : BaseFragment<FragmentProductInformationBinding>(
    FragmentProductInformationBinding::inflate
) {

    private lateinit var viewModel: ProductDetailViewModel
    private lateinit var adapter: ProductInformationAdapter
    private var barcode: String? = null
    private var isLoading: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcode = arguments?.getString(Constants.ARG_BARCODE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecycleView()
        setupListeners()
        observeData()
    }

    private fun setupViewModel() {
        val app = (requireActivity().application as BiteWiseApplication)
        val factory = ViewModelFactory(
            application = app,
            productRepository = app.productRepository,
            userRepository = app.userRepository,
            healthScoringEngine = app.healthScoringEngine,
            aiRepository = app.aiRepository,
            recentProductRepository = app.recentProductRepository
        )
        viewModel = ViewModelProvider(this, factory)[ProductDetailViewModel::class.java]
    }

    private fun setupRecycleView() {
        adapter = ProductInformationAdapter(
            rows = emptyList(),
            onContainerClick = { clickedContainer ->
                Log.d("ProductDetail", "Clicked: $clickedContainer")
            }
        )
        binding.recyclerProductInformation.layoutManager =
            LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false)
        binding.recyclerProductInformation.adapter = adapter

        barcode?.let { code ->
            if (viewModel.productState.value !is UiState.Success) {
                viewModel.fetchProduct(code)
            }
        }
    }

    private fun observeData() {
        viewModel.productState
            .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                when (state) {
                    is UiState.Loading -> {
                        if (!isLoading) {
                            Log.d("ProductDetail", "Loading...")
                            isLoading = true
                        }
                    }
                    is UiState.Success -> {
                        isLoading = false
                        Log.d("ProductDetail", "Success: ${state.data}")
                        val (product, analysis) = state.data
                        val rows: List<ProductRowManager> = listOf(
                            ProductRowManager.Header(product, analysis),
                            ProductRowManager.FirstContainer(product, analysis),
                            ProductRowManager.SecondContainer(product),
                            ProductRowManager.ThirdContainer(product),
                            ProductRowManager.FourthContainer(product),
                            ProductRowManager.NutritionContainer(product)
                        )
                        adapter.updateRows(rows)
                    }
                    is UiState.Error -> {
                        isLoading = false
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}
