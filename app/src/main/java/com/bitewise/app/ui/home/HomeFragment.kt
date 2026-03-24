package com.bitewise.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.R
import com.bitewise.app.data.local.di.DatabaseModule
import com.bitewise.app.data.repository.LocalProductRepository
import com.bitewise.app.databinding.FragmentHomeScreenBinding
import com.bitewise.app.ui.home.adapters.HorizontalFoodTile
import com.bitewise.app.ui.home.adapters.HorizontalTileAdapter
import com.bitewise.app.ui.product.ProductViewModel
import com.bitewise.app.ui.product.ProductViewModelFactory
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home_screen) {

    private var _binding: FragmentHomeScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: HorizontalTileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeScreenBinding.bind(view)

        // Recycler setup
        adapter = HorizontalTileAdapter(mutableListOf())
        binding.recyclerRecentlyViewed.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerRecentlyViewed.adapter = adapter

        val dao = DatabaseModule.getDatabase(requireContext()).productDao()
        val repository = LocalProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        // Example recent barcodes
        val recentBarcodes = listOf("4800361410816", "4800092113338")

        lifecycleScope.launch {
            recentBarcodes.forEach { barcode ->
                val product = repository.getProductByBarcode(barcode)

                product?.let {
                    val tile = HorizontalFoodTile(
                        id = it.code,
                        name = it.name ?: "Unknown",
                        image = R.drawable.ic_launcher_foreground
                    )
                    adapter.addItem(tile)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}