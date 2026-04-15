package com.bitewise.app.feature.onboarding.ui.health

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.MultiAutoCompleteTextView
import androidx.fragment.app.activityViewModels
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.MainActivity
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.databinding.Onboarding3Binding
import com.bitewise.app.domain.user.models.UserConstants
import com.bitewise.app.feature.onboarding.OnboardingViewModel
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

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
            binding.chipGroupAllergies,
            UserConstants.ALLERGIES,
            viewModel.data.allergies.toMutableList()
        ) { viewModel.data.allergies = it }

        setupSearchAndChip(
            binding.multiDiet,
            binding.chipGroupDiet,
            UserConstants.DIET_TYPES,
            viewModel.data.dietaryPreferences.toMutableList()
        ) { viewModel.data.dietaryPreferences = it }

        setupSearchAndChip(
            binding.multiConditions,
            binding.chipGroupConditions,
            UserConstants.MEDICAL_CONDITIONS,
            viewModel.data.medicalConditions.toMutableList()
        ) { viewModel.data.medicalConditions = it }

        binding.btnNext.setOnClickListener {
            viewModel.completeOnboarding()
            finishOnboarding()
        }

        binding.btnSkip.setOnClickListener {
            viewModel.completeOnboarding()
            finishOnboarding()
        }
    }

    /**
     * Sets up a search-based MultiAutoCompleteTextView that converts selections into Chips.
     */
    private fun setupSearchAndChip(
        textView: MultiAutoCompleteTextView,
        chipGroup: ChipGroup,
        suggestions: List<String>,
        selectedList: MutableList<String>,
        onUpdate: (List<String>) -> Unit
    ) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, suggestions)
        textView.setAdapter(adapter)
        textView.setTokenizer(MultiAutoCompleteTextView.CommaTokenizer())
        
        // Show dropdown immediately on focus/type
        textView.threshold = 1 

        textView.setOnItemClickListener { _, _, _, _ ->
            // Extract the last typed item from the comma-separated string
            val fullText = textView.text.toString()
            val parts = fullText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            val lastAdded = parts.lastOrNull() ?: ""

            if (suggestions.contains(lastAdded) && !selectedList.contains(lastAdded)) {
                selectedList.add(lastAdded)
                addChipToGroup(chipGroup, lastAdded, selectedList, onUpdate)
                onUpdate(selectedList)
            }
            // Clear the text field after selection to keep UI clean
            textView.setText("")
        }

        // Initialize with existing data (e.g., OMNIVORE by default)
        chipGroup.removeAllViews()
        selectedList.forEach { item ->
            addChipToGroup(chipGroup, item, selectedList, onUpdate)
        }
    }

    private fun addChipToGroup(
        chipGroup: ChipGroup,
        text: String,
        selectedList: MutableList<String>,
        onUpdate: (List<String>) -> Unit
    ) {
        val chip = Chip(requireContext()).apply {
            this.text = text
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                chipGroup.removeView(this)
                selectedList.remove(text)
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
