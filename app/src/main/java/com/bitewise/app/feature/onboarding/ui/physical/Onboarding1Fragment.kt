package com.bitewise.app.feature.onboarding.ui.physical

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.R
import com.bitewise.app.databinding.Onboarding1Binding
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.feature.onboarding.OnboardingViewModel
class Onboarding1Fragment : BaseFragment<Onboarding1Binding>(
    Onboarding1Binding::inflate
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

        binding.btnNext.setOnClickListener {
            saveData()
            if (viewModel.isPhysicalValid()) {
                findNavController().navigate(R.id.action_onboarding1_to_onboarding2)
            }
        }

        binding.btnSkip.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding1_to_onboarding2)
        }
    }

    private fun saveData() {
        viewModel.data.age = binding.etAge.text.toString().toIntOrNull()
        viewModel.data.weight = binding.etWeight.text.toString().toFloatOrNull()
        viewModel.data.height = binding.etHeight.text.toString().toFloatOrNull()
    }
}
