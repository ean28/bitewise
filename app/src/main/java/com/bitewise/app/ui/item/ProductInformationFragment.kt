package com.bitewise.app.ui.item

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.R
import com.bitewise.app.data.local.di.LocalProductDatabaseModule
import com.bitewise.app.data.repository.LocalProductRepository
import com.bitewise.app.databinding.FragmentProductInformationBinding
import com.bitewise.app.ui.item.adapter.ProductInformationAdapter
import com.bitewise.app.ui.item.adapter.rows.ProductRowManager
import com.bitewise.app.ui.product.ProductViewModel
import com.bitewise.app.ui.product.ProductViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProductInformationFragment : Fragment(R.layout.fragment_product_information) {

    private var _binding: FragmentProductInformationBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel
    private var barcode: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        barcode = arguments?.getString(ARG_BARCODE)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentProductInformationBinding.bind(view)

        val dao = LocalProductDatabaseModule.getDatabase(requireContext()).productDao()
        val repository = LocalProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        binding.recyclerProductInformation.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        barcode?.let { code ->
            viewModel.fetchProduct(code)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.product.collectLatest { product ->
                product?.let { p ->
                    val rows: List<ProductRowManager> = listOf(
                        ProductRowManager.Header(p),
                        ProductRowManager.FirstContainer(p),
                        ProductRowManager.SecondContainer(p)
                    )

                    // Set up adapter
                    val adapter = ProductInformationAdapter(
                        rows = rows,
                        onContainerClick = { clickedContainer ->
                            when (clickedContainer) {
                                is ProductRowManager.FirstContainer -> {
                                    println("First container clicked: ${clickedContainer.product.name}")
                                }
                                is ProductRowManager.SecondContainer -> {
                                    println("Second container clicked: ${clickedContainer.product.name}")
                                }
                                else -> {}
                            }
                        }
                    )

                    binding.recyclerProductInformation.adapter = adapter
                }
            }
        }

        // Back button
        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_BARCODE = "arg_barcode"
    }
}