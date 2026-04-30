package com.bitewise.app.feature.onboarding.ui.lifestyle

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.R
import com.bitewise.app.databinding.Onboarding2Binding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.feature.onboarding.OnboardingViewModel
import com.bitewise.app.domain.user.models.UserConstants

class Onboarding2Fragment : BaseFragment<Onboarding2Binding>(
    Onboarding2Binding::inflate
) {
    private val viewModel: OnboardingViewModel by activityViewModels {
        val app = requireActivity().application as BiteWiseApplication
        ViewModelFactory(
            application = app,
            productRepository = app.productRepository,
            userRepository = app.userRepository,
            settingsRepository = app.settingsRepository
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupActivitySpinner()

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding2_to_onboarding3)
        }

        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding2_to_onboarding3)
        }
    }

    private fun setupActivitySpinner() {
        val levels = UserConstants.ACTIVITY_LEVELS.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, levels)
        binding.spinnerActivity.setAdapter(adapter)

        binding.spinnerActivity.setOnItemClickListener { _, _, position, _ ->
            val selected = levels[position]
            viewModel.data.activityLevel = selected
            binding.txtActivityDesc.text = UserConstants.ACTIVITY_LEVELS[selected]
        }
        
        // Restore if exists
        viewModel.data.activityLevel?.let {
            binding.spinnerActivity.setText(it, false)
            binding.txtActivityDesc.text = UserConstants.ACTIVITY_LEVELS[it]
        }
    }
}
