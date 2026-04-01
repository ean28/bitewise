package com.bitewise.app.ui.product

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.databinding.FragmentProductInformationBinding
import com.bitewise.app.ui.common.BaseFragment
import com.bitewise.app.ui.common.UiState
import com.bitewise.app.ui.common.ViewModelFactory
import com.bitewise.app.ui.product.adapter.ProductInformationAdapter
import com.bitewise.app.ui.product.adapter.rows.ProductRowManager
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

        val app = (requireActivity().application as BiteWiseApplication)
        val factory = ViewModelFactory(app.productRepository, app.recentHistory)
        viewModel = ViewModelProvider(this, factory)[ProductDetailViewModel::class.java]

        binding.recyclerProductInformation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        barcode?.let { code ->
            viewModel.fetchProduct(code)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.productState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // TODO: show loading
                    }
                    is UiState.Success -> {
                        val product = state.data
                        val rows: List<ProductRowManager> = listOf(
                            ProductRowManager.Header(product),
                            ProductRowManager.FirstContainer(product),
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

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    companion object {
        private const val ARG_BARCODE = "arg_barcode"
    }
}
