package com.bitewise.app.ui.search

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
import com.bitewise.app.ui.common.BaseFragment
import com.bitewise.app.ui.common.UiState
import com.bitewise.app.ui.common.ViewModelFactory
import com.bitewise.app.ui.search.adapter.SearchTileAdapter
import com.bitewise.app.ui.search.utils.CountryList
import com.bitewise.app.ui.search.utils.setupCountryPicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : BaseFragment<FragmentSearchScreenBinding>(
    FragmentSearchScreenBinding::inflate
) {

    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchTileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = (requireActivity().application as BiteWiseApplication).productRepository
        val factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]

        adapter = SearchTileAdapter { selectedProduct ->
            val bundle = Bundle().apply {
                putString("arg_barcode", selectedProduct.code)
            }

            findNavController().navigate(
                com.bitewise.app.R.id.action_nav_search_to_productDetailFragment,
                bundle
            )
        }

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
            android.R.layout.simple_spinner_dropdown_item,
            CountryList.COUNTRIES
        )

        binding.countryDropdown.setAdapter(countryAdapter)
        binding.countryDropdown.setOnClickListener {
            binding.countryDropdown.showDropDown()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> {
                        // TODO: show loading indicator
                    }
                    is UiState.Success -> {
                        val results = state.data
                        adapter.setItems(results)
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
