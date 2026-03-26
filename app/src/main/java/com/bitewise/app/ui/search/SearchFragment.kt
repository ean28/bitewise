package com.bitewise.app.ui.search

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bitewise.app.R
import com.bitewise.app.data.local.di.LocalProductDatabaseModule
import com.bitewise.app.data.repository.LocalProductRepository
import com.bitewise.app.databinding.FragmentSearchScreenBinding
import com.bitewise.app.ui.product.ProductViewModel
import com.bitewise.app.ui.product.ProductViewModelFactory
import com.bitewise.app.ui.search.adapter.SearchTileAdapter
import com.bitewise.app.ui.search.utils.CountryList
import com.bitewise.app.ui.search.utils.setupCountryPicker
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SearchFragment : Fragment(R.layout.fragment_search_screen) {

    private var _binding: FragmentSearchScreenBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: SearchTileAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSearchScreenBinding.bind(view)

        val dao = LocalProductDatabaseModule.getDatabase(requireContext()).productDao()
        val repository = LocalProductRepository(dao)
        val factory = ProductViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        //for click handling
        adapter = SearchTileAdapter { product ->
            println("Clicked: ${product.name}")
        }

        binding.recyclerFoodGrid.layoutManager =
            GridLayoutManager(requireContext(), 3)
        binding.recyclerFoodGrid.adapter = adapter

        viewModel.fetchAllProducts()

        // for country selection TODO("make this adaptive")
        val phFlag = CountryList.COUNTRIES.first {
            it.name == "Philippines"
        }.flag

        binding.countryDropdown.setText(
            getString(R.string.country_dropdown_format, phFlag), false
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


        // real time search
//        binding.searchInput.doAfterTextChanged { text ->
//            val query = text.toString()
//            if (query.isNotBlank()) {
//                viewModel.search(query)
//            }
//        }


        lifecycleScope.launch {
            viewModel.searchResults.collectLatest { results ->
                adapter.setItems(results)

                binding.txtSearchResults.text = "${results.size} results found"
            }
        }
    }

    private fun filterResultsByCountry(countryName: String) {
        // TODO("add a complete dataset")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}