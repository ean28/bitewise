package com.bitewise.app.feature.product.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.databinding.FragmentProductInformationBinding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.UiState
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.feature.product.ui.adapter.ProductInformationAdapter
import com.bitewise.app.feature.product.ui.adapter.rows.ProductRowManager
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductDetailFragment : BaseFragment<FragmentProductInformationBinding>(
    FragmentProductInformationBinding::inflate
) {

    private lateinit var viewModel: ProductDetailViewModel
    private var barcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcode = arguments?.getString(ARG_BARCODE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupRecycleView()
        setupListeners()
        observeData()
    }

    private fun setupViewModel(){
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

    private fun setupRecycleView(){
        binding.recyclerProductInformation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        barcode?.let { code ->
            viewModel.fetchProduct(code)
        }
    }

    private fun observeData(){

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // TODO: show loading
                        binding.recyclerProductInformation.adapter = null
                        Log.d("ProductDetail", "Loading...")
                    }
                    is UiState.Success -> {

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

                        val adapter = ProductInformationAdapter(
                            rows = rows,
                            onContainerClick = { clickedContainer ->
                                Log.d("ProductDetail", "Clicked: $clickedContainer")
                            }
                        )
                        binding.recyclerProductInformation.adapter = adapter
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun setupListeners(){
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
    companion object {
        private const val ARG_BARCODE = "arg_barcode"
    }
}
