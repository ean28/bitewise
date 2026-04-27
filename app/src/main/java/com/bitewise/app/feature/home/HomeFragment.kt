package com.bitewise.app.feature.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.R
import com.bitewise.app.databinding.FragmentHomeScreenBinding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.Constants
import com.bitewise.app.core.UiState
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.feature.home.ui.HorizontalTileAdapter
import com.bitewise.app.feature.home.ui.VerticalRecommendationAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : BaseFragment<FragmentHomeScreenBinding>(
    FragmentHomeScreenBinding::inflate
) {
    private lateinit var viewModel: HomeViewModel
    private lateinit var recentAdapter: HorizontalTileAdapter
    private lateinit var recommendationAdapter: VerticalRecommendationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerViews()
        setupListeners()
        observeData()
        //TODO: Create pulldown refresh fun
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadRecentItems()
    }

    private fun setupViewModel() {
        val app = requireActivity().application as BiteWiseApplication
        val factory = ViewModelFactory(
            application = app,
            productRepository = app.productRepository,
            userRepository = app.userRepository,
            healthScoringEngine = app.healthScoringEngine,
            aiRepository = app.aiRepository,
            recentProductRepository = app.recentProductRepository,
            settingsRepository = app.settingsRepository
        )
        viewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
    }

    private fun setupRecyclerViews() {
        recentAdapter = HorizontalTileAdapter { product ->
            val bundle = Bundle().apply { putString(Constants.ARG_BARCODE, product.code) }
            findNavController().navigate(R.id.action_nav_home_to_nav_product_detail, bundle)
        }
        binding.recyclerRecentlyViewed.layoutManager = 
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerRecentlyViewed.adapter = recentAdapter

        recommendationAdapter = VerticalRecommendationAdapter { scoredProduct ->
            val bundle = Bundle().apply { putString(Constants.ARG_BARCODE, scoredProduct.product.code) }
            findNavController().navigate(R.id.action_nav_home_to_nav_product_detail, bundle)
        }
        binding.recyclerRecommendations.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerRecommendations.adapter = recommendationAdapter
    }

    private fun setupListeners() {
        binding.btnSafetyStatus.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_ai_sync)
        }
        
        binding.btnSyncNow.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_ai_sync)
        }

        binding.btnNotNow.setOnClickListener {
            viewModel.dismissSyncPrompt()
        }

        binding.btnRecommendationSync.setOnClickListener {
            findNavController().navigate(R.id.action_nav_home_to_nav_ai_sync)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.recentProducts.collectLatest { products ->
                        recentAdapter.setItems(products)
                    }
                }
                launch {
                    viewModel.recommendedProducts.collectLatest { state ->
                        if (state is UiState.Success) {
                            recommendationAdapter.setItems(state.data)
                            binding.txtRecommendationCount.text = "(${state.data.size} items)"
                        }
                    }
                }
                launch {
                    viewModel.userContext.collectLatest { user ->
                        binding.txtStatusContent.text = if (user != null) "Active Profile" else "Setup Required"
                    }
                }
                launch {
                    viewModel.suggestionText.collectLatest { suggestion ->
                        binding.txtSuggestionContent.text = suggestion
                    }
                }
                launch {
                    viewModel.showSyncPrompt.collectLatest { show ->
                        binding.cardSyncPrompt.visibility = if (show) View.VISIBLE else View.GONE
                        // Show the small sync button if the prompt is hidden but user hasn't synced
                        binding.btnRecommendationSync.visibility = if (!show && viewModel.isSyncPromptDismissed.value) View.VISIBLE else View.GONE
                    }
                }
            }
        }
    }
}
