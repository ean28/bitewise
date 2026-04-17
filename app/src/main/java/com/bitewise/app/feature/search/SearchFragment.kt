package com.bitewise.app.feature.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.databinding.FragmentSearchScreenBinding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.UiState
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.feature.search.ui.SearchTileAdapter
import com.bitewise.app.feature.search.ui.CountryList
import com.bitewise.app.feature.search.ui.setupCountryPicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<FragmentSearchScreenBinding>(
    FragmentSearchScreenBinding::inflate
) {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchTileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        saveRecentProductClickToRecent()
        setupRecyclerView()
        observeData()
    }

    private fun setupViewModel() {
        val app = requireActivity().application as BiteWiseApplication
        val factory = ViewModelFactory(
            application = app,
            productRepository = app.productRepository,
            aiRepository = app.aiRepository
        )
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    private fun setupRecyclerView() {
        binding.recyclerFoodGrid.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.recyclerFoodGrid.adapter = adapter

        if (viewModel.searchState.value is UiState.Loading) {
            viewModel.fetchAllProducts()
        }

        val phFlag = CountryList.COUNTRIES.first {
            it.name == "Philippines"
        }.flag

        binding.countryDropdown.setText(
            getString(com.bitewise.app.R.string.country_dropdown_format, phFlag), false
        )

        binding.countryDropdown.setupCountryPicker(CountryList.COUNTRIES) {
            filterResultsByCountry(it.name)
        }

        val countryAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            CountryList.COUNTRIES
        )
        binding.countryDropdown.setAdapter(countryAdapter)

        binding.countryDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countryAdapter.getItem(position)
            selectedCountry?.let {
                filterResultsByCountry(it.name)
            }
        }
    }

    private fun saveRecentProductClickToRecent() {
        adapter = SearchTileAdapter { selectedProduct : SearchTileUiState ->
            val bundle = Bundle().apply {
                putString("arg_barcode", selectedProduct.product.code)
            }

            findNavController().navigate(
                com.bitewise.app.R.id.action_nav_search_to_productDetailFragment,
                bundle
            )
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // TODO: show loading indicator
                    }
                    is UiState.Success -> {
                        val results = state.data
                        adapter.submitList(results)
                        binding.txtSearchResults.text = "${results.size} results found"
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun filterResultsByCountry(countryName: String) {
        // TODO: implement filtering logic
    }
}
