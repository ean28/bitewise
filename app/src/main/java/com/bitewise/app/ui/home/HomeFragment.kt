package com.bitewise.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.R
import com.bitewise.app.databinding.FragmentHomeScreenBinding
import com.bitewise.app.ui.common.BaseFragment
import com.bitewise.app.ui.common.ViewModelFactory
import com.bitewise.app.ui.home.adapters.HorizontalTileAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeScreenBinding>(
    FragmentHomeScreenBinding::inflate
) {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: HorizontalTileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recycler setup
        adapter = HorizontalTileAdapter { selectedProduct ->
            val bundle = Bundle().apply {
                putString("arg_barcode", selectedProduct.code)
            }
            findNavController().navigate(
                R.id.action_nav_home_to_nav_product_detail,
                bundle
            )
        }
        
        binding.recyclerRecentlyViewed.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerRecentlyViewed.adapter = adapter

        val app = requireActivity().application as BiteWiseApplication
        val factory = ViewModelFactory(app.productRepository, app.recentHistory)
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.recentProducts.collectLatest { products ->
                adapter.setItems(products)
                
                binding.recyclerRecentlyViewed.visibility =
                    if (products.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRecentItems()
    }
}
