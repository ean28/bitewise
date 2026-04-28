package com.bitewise.app.feature.settings.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.databinding.FragmentDatabaseInspectorBinding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DatabaseInspectorFragment : BaseFragment<FragmentDatabaseInspectorBinding>(
    FragmentDatabaseInspectorBinding::inflate
) {
    private lateinit var viewModel: DatabaseInspectorViewModel
    private lateinit var adapter: AiAnalysisAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupRecyclerView()
        setupListeners()
        observeData()
    }

    private fun setupViewModel() {
        val app = requireActivity().application as BiteWiseApplication
        val factory = ViewModelFactory(
            application = app,
            productRepository = app.productRepository,
            userRepository = app.userRepository,
            aiRepository = app.aiRepository
        )
        viewModel = ViewModelProvider(this, factory)[DatabaseInspectorViewModel::class.java]
    }

    private fun setupRecyclerView() {
        adapter = AiAnalysisAdapter()
        binding.recyclerAiAnalyses.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAiAnalyses.adapter = adapter
    }

    private fun setupListeners() {
        binding.btnClearAiDb.setOnClickListener {
            viewModel.clearAiAnalyses()
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userContext
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { user ->
                    binding.txtUserProfileData.text = user?.let {
                        """
                    Age: ${it.age}
                    Weight: ${it.weight}
                    Height: ${it.height}
                    Activity: ${it.activity}
                    Allergies: ${it.allergies}
                    Dietary: ${it.dietary}
                    Conditions: ${it.conditions}
                    Hash: ${it.hashCode()}
                    """.trimIndent()
                    } ?: "No User Profile Data Found"
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            viewModel.aiAnalyses
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { analyses ->
                    adapter.submitList(analyses)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}
