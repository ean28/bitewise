package com.bitewise.app.feature.ai

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.R
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.databinding.FragmentAiSyncBinding
import com.bitewise.app.feature.ai.ui.SyncLogAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class AiSyncFragment : BaseFragment<FragmentAiSyncBinding>(
    FragmentAiSyncBinding::inflate
) {
    private lateinit var viewModel: AiSyncViewModel
    private lateinit var logAdapter: SyncLogAdapter

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
            aiRepository = app.aiRepository,
            healthScoringEngine = app.healthScoringEngine,
            settingsRepository = app.settingsRepository
        )
        viewModel = ViewModelProvider(this, factory)[AiSyncViewModel::class.java]
    }

    private fun setupRecyclerView() {
        logAdapter = SyncLogAdapter()
        binding.recyclerSyncLogs.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerSyncLogs.adapter = logAdapter
    }

    private fun setupListeners() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.sliderBatchSize.addOnChangeListener { _, value, _ ->
            viewModel.setBatchSize(value.toInt())
            binding.txtBatchSizeLabel.text = "AI Batch Size: ${value.toInt()}"
        }

        binding.sliderQuickSyncLimit.addOnChangeListener { _, value, _ ->
            viewModel.setQuickSyncLimit(value.toInt())
            binding.txtQuickSyncLabel.text = "Quick Sync Limit: ${value.toInt()}"
        }

        binding.btnQuickSync.setOnClickListener {
            viewModel.startSync(isQuick = true)
        }

        binding.btnStartSync.setOnClickListener {
            viewModel.startSync(isQuick = false)
        }

        binding.btnResumeSync.setOnClickListener {
            viewModel.resumeSync()
        }

        binding.btnCancelResume.setOnClickListener {
            showDiscardProgressConfirmation()
        }

        binding.btnStopSync.setOnClickListener {
            showStopSyncConfirmation()
        }

        binding.btnClearCache.setOnClickListener {
            viewModel.clearCache()
        }
    }

    private fun showStopSyncConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Stop Sync?")
            .setMessage("Are you sure you want to stop the current AI sync process? Progress made so far will be saved and can be resumed later.")
            .setPositiveButton("Stop Sync") { _, _ ->
                viewModel.stopSync()
            }
            .setNegativeButton("Keep Syncing", null)
            .show()
    }

    private fun showDiscardProgressConfirmation() {
        AlertDialog.Builder(requireContext())
            .setTitle("Discard Progress?")
            .setMessage("Are you sure you want to discard your current sync progress? This cannot be undone.")
            .setPositiveButton("Discard") { _, _ ->
                viewModel.cancelInterruptedSync()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { state ->
                        binding.txtFreshCount.text = state.freshCount.toString()
                        binding.txtStaleCount.text = state.staleCount.toString()
                        binding.txtEstTokens.text = "~${state.estTokens}"
                    }
                }

                launch {
                    viewModel.isUserComplete.collectLatest { isComplete ->
                        updateActionButtonsState(isComplete)
                        
                        if (!isComplete) {
                            binding.cardDebugInfo.visibility = View.VISIBLE
                            binding.txtDebugReason.text = "Reason: User profile is incomplete (Age, Weight, Height, and Activity are required)."
                        } else {
                            binding.cardDebugInfo.visibility = View.GONE
                        }
                    }
                }

                launch {
                    viewModel.hasInterruptedSync.collectLatest { hasInterrupted ->
                        binding.cardResumePrompt.visibility = if (hasInterrupted) View.VISIBLE else View.GONE
                    }
                }

                launch {
                    viewModel.syncLogs.collectLatest { logs ->
                        logAdapter.submitList(logs)
                    }
                }

                launch {
                    viewModel.workInfos.collectLatest { infos ->
                        val active = infos.any { !it.state.isFinished }
                        if (active) {
                            binding.layoutProgress.visibility = View.VISIBLE
                            binding.progressIndicator.isIndeterminate = true
                            binding.btnStartSync.isEnabled = false
                            binding.btnQuickSync.isEnabled = false
                            binding.btnResumeSync.isEnabled = false
                        } else {
                            binding.layoutProgress.visibility = View.GONE
                            updateActionButtonsState(viewModel.isUserComplete.value)
                            
                            val failed = infos.firstOrNull { it.state == WorkInfo.State.FAILED }
                            if (failed != null) {
                                binding.cardDebugInfo.visibility = View.VISIBLE
                                binding.txtDebugReason.text = "Last Sync Failed. Check logs or network."
                            }
                        }
                        
                        val activeWork = infos.firstOrNull { !it.state.isFinished }
                        activeWork?.progress?.let { progress ->
                            val current = progress.getInt("progress", 0)
                            val total = progress.getInt("total", 0)
                            if (total > 0) {
                                binding.progressIndicator.isIndeterminate = false
                                binding.progressIndicator.progress = current
                                binding.progressIndicator.max = total
                                binding.txtProgressStatus.text = "Syncing: $current/$total"
                            }
                        }
                    }
                }
            }
        }
    }

    private fun updateActionButtonsState(isUserComplete: Boolean) {
        binding.btnStartSync.isEnabled = isUserComplete
        binding.btnQuickSync.isEnabled = isUserComplete
        binding.btnResumeSync.isEnabled = isUserComplete
    }
}
