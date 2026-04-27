package com.bitewise.app.feature.onboarding.ui.health

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.MainActivity
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.databinding.Onboarding3Binding
import com.bitewise.app.domain.user.models.UserConstants
import com.bitewise.app.feature.onboarding.OnboardingViewModel
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class Onboarding3Fragment : BaseFragment<Onboarding3Binding>(
    Onboarding3Binding::inflate
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

        setupSearchAndChip(
            binding.multiAllergies,
            binding.btnAddAllergy,
            binding.chipGroupAllergies,
            UserConstants.ALLERGIES,
            viewModel.data.allergies.toMutableList()
        ) { viewModel.data.allergies = it }

        setupSearchAndChip(
            binding.multiDiet,
            binding.btnAddDiet,
            binding.chipGroupDiet,
            UserConstants.DIET_TYPES,
            viewModel.data.dietaryPreferences.toMutableList()
        ) { viewModel.data.dietaryPreferences = it }

        setupSearchAndChip(
            binding.multiConditions,
            binding.btnAddCondition,
            binding.chipGroupConditions,
            UserConstants.MEDICAL_CONDITIONS,
            viewModel.data.medicalConditions.toMutableList()
        ) { viewModel.data.medicalConditions = it }

        binding.btnNext.setOnClickListener {
            viewModel.completeOnboarding()
        }

        binding.btnSkip.setOnClickListener {
            viewModel.completeOnboarding()
        }

        observeNavigation()
    }

    private fun observeNavigation() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.onboardingCompleted.collectLatest {
                    finishOnboarding()
                }
            }
        }
    }

    private fun setupSearchAndChip(
        textView: MultiAutoCompleteTextView,
        addButton: MaterialButton,
        chipGroup: ChipGroup,
        suggestions: List<String>,
        selectedList: MutableList<String>,
        onUpdate: (List<String>) -> Unit
    ) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions)
        textView.setAdapter(adapter)
        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        textView.threshold = 1 

        val addItem = {
            val fullText = textView.text.toString()
            val itemsToAdd = fullText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            
            var changed = false
            itemsToAdd.forEach { value ->
                if (selectedList.none { it.equals(value, ignoreCase = true) }) {
                    selectedList.add(value)
                    addChipToGroup(chipGroup, value, selectedList, onUpdate)
                    changed = true
                }
            }
            
            if (changed) {
                onUpdate(selectedList)
            }
            textView.setText("")
        }

        textView.setOnItemClickListener { _, _, _, _ -> addItem() }
        addButton.setOnClickListener { addItem() }

        // Initialize with existing data
        chipGroup.removeAllViews()
        selectedList.forEach { item ->
            addChipToGroup(chipGroup, item, selectedList, onUpdate)
        }
    }

    private fun addChipToGroup(
        chipGroup: ChipGroup,
        value: String,
        selectedList: MutableList<String>,
        onUpdate: (List<String>) -> Unit
    ) {
        val chip = Chip(requireContext()).apply {
            this.text = value
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                chipGroup.removeView(this)
                selectedList.remove(value)
                onUpdate(selectedList)
            }
        }
        chipGroup.addView(chip)
    }

    private fun finishOnboarding() {
        activity?.let {
            val intent = Intent(it, MainActivity::class.java)
            startActivity(intent)
            it.finish()
        }
    }
}
