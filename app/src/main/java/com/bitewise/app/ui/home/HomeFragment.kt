package com.bitewise.app.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bitewise.app.R
import com.bitewise.app.data.network.RetrofitInstance.api
import com.bitewise.app.data.network.repository.ProductRepository
import com.bitewise.app.ui.home.adapters.HorizontalFoodTile
import com.bitewise.app.ui.home.adapters.HorizontalTileAdapter
import com.bitewise.app.ui.product.ProductViewModel
import com.bitewise.app.ui.product.ProductViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home_screen) {

    private lateinit var viewModel: ProductViewModel
    private lateinit var adapter: HorizontalTileAdapter
    private lateinit var recentSearchedRecycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository: ProductRepository = ProductRepository(api)
        val factory: ProductViewModelFactory = ProductViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[ProductViewModel::class.java]

        recentSearchedRecycler = view.findViewById<RecyclerView>(R.id.recycler_recently_viewed)
        recentSearchedRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL, false
        )

        adapter = HorizontalTileAdapter(mutableListOf())
        recentSearchedRecycler.adapter = adapter

        /**
        val recentlySearchedItems: List<HorizontalFoodTile> =
        listOf(
        HorizontalFoodTile("1", "SKY FLAKES", R.drawable.ic_launcher_foreground),
        HorizontalFoodTile("2", "MILO", R.drawable.ic_launcher_foreground),
        HorizontalFoodTile("3", "QUAKES", R.drawable.ic_launcher_foreground),
        HorizontalFoodTile("4", "AAA", R.drawable.ic_launcher_foreground),
        HorizontalFoodTile("5", "AA", R.drawable.ic_launcher_foreground),
        HorizontalFoodTile("6", "312as", R.drawable.ic_launcher_foreground)
        )

        recentSearchedRecycler.adapter = HorizontalTileAdapter(recentlySearchedItems)
         * */

        viewModel.product.observe(viewLifecycleOwner) { product ->
            product?.let {
                val tile = HorizontalFoodTile(
                    id = it.productName,
                    name = it.productName,
                    image = R.drawable.ic_launcher_foreground
                )
                adapter.addItem(tile)
            }
        }
        val recentBarcodes = listOf("4800361410816", "4800092113338")
        //fetch
        recentBarcodes.forEach { barcode ->
            viewModel.fetchProduct(barcode)

        }
    }
}