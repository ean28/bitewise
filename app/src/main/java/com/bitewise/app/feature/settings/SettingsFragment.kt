package com.bitewise.app.feature.settings

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bitewise.app.BiteWiseApplication
import com.bitewise.app.R
import com.bitewise.app.core.BaseFragment
import com.bitewise.app.core.ViewModelFactory
import com.bitewise.app.databinding.FragmentSettingsBinding
import com.bitewise.app.domain.user.models.UserConstants
import com.bitewise.app.domain.user.models.UserContext
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(
    FragmentSettingsBinding::inflate
) {
    private lateinit var viewModel: SettingsViewModel

    private val selectedDiets = mutableListOf<String>()
    private val selectedAllergies = mutableListOf<String>()
    private val selectedConditions = mutableListOf<String>()
    private var selectedActivityLevel: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()
        setupSpinners()
        setupMultiSelects()
        setupListeners()
        observeData()
    // TODO("ADD UI STATES")
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
        viewModel = ViewModelProvider(this, factory)[SettingsViewModel::class.java]
    }

    private fun setupSpinners() {
        val levels = UserConstants.ACTIVITY_LEVELS.keys.toList()
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, levels)
        binding.spinnerActivity.setAdapter(adapter)

        binding.spinnerActivity.setOnItemClickListener { _, _, position, _ ->
            selectedActivityLevel = levels[position]
            binding.txtActivityDesc.text = UserConstants.ACTIVITY_LEVELS[selectedActivityLevel]
        }
    }

    private fun setupMultiSelects() {
        setupMultiSelectLogic(
            binding.multiDiet,
            binding.btnAddDiet,
            binding.chipGroupDiet,
            UserConstants.DIET_TYPES,
            selectedDiets
        )
        setupMultiSelectLogic(
            binding.multiAllergies,
            binding.btnAddAllergy,
            binding.chipGroupAllergies,
            UserConstants.ALLERGIES,
            selectedAllergies
        )
        setupMultiSelectLogic(
            binding.multiConditions,
            binding.btnAddCondition,
            binding.chipGroupConditions,
            UserConstants.MEDICAL_CONDITIONS,
            selectedConditions
        )
    }

    private fun setupMultiSelectLogic(
        textView: AutoCompleteTextView,
        addButton: MaterialButton,
        chipGroup: ChipGroup,
        suggestions: List<String>,
        selectedList: MutableList<String>
    ) {
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            suggestions)
        textView.setAdapter(adapter)

        val addItem = {
            val fullText = textView.text.toString()
            val itemsToAdd = fullText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
            
            var changed = false
            itemsToAdd.forEach { value ->
                if (selectedList.none { it == value }) {
                    selectedList.add(value)
                    changed = true
                }
            }
            
            if (changed) {
                refreshChips(chipGroup, selectedList)
            }
            textView.setText("")
        }

        textView.setOnItemClickListener { _, _, _, _ -> addItem() }
        addButton.setOnClickListener { addItem() }
    }

    private fun refreshChips(chipGroup: ChipGroup, selectedList: MutableList<String>) {
        chipGroup.removeAllViews()
        if (selectedList.isEmpty()) {
            val emptyChip = Chip(requireContext()).apply {
                text = "None"
                isEnabled = false
                setChipBackgroundColorResource(R.color.chip_background_empty)
            }
            chipGroup.addView(emptyChip)
            return
        }

        selectedList.forEach { value ->
            val chip = Chip(requireContext()).apply {
                this.text = value
                isCloseIconVisible = true
                setChipBackgroundColorResource(R.color.chip_background_selected)
                setCloseIconTintResource(R.color.chip_close_icon_tint)
                setOnCloseIconClickListener {
                    selectedList.remove(value)
                    refreshChips(chipGroup, selectedList)
                }
            }
            chipGroup.addView(chip)
        }
    }

    private fun setupListeners() {
        binding.btnSaveSettings.setOnClickListener {
            val age = binding.editAge.text.toString().toIntOrNull() ?: 0
            val weight = binding.editWeight.text.toString().toDoubleOrNull() ?: 0.0
            val height = binding.editHeight.text.toString().toDoubleOrNull() ?: 0.0

            val newContext = UserContext(
                age = age,
                weight = weight,
                height = height,
                activity = selectedActivityLevel,
                conditions = selectedConditions.toList(),
                dietary = selectedDiets.toList(),
                allergies = selectedAllergies.toList()
            )

            viewModel.saveUserContext(newContext)
            Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
        }

        binding.btnInspectDb.setOnClickListener {
            findNavController().navigate(R.id.action_nav_settings_to_nav_database_inspector)
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.userContext
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .onEach { user ->
                    user?.let {
                        binding.editAge.setText(
                            if (it.age > 0)
                                it.age.toString()
                            else
                                ""
                        )
                        binding.editWeight.setText(if (it.weight > 0.0) it.weight.toString() else "")
                        binding.editHeight.setText(if (it.height > 0.0) it.height.toString() else "")

                        // Activity Level
                        selectedActivityLevel = it.activity
                        if (selectedActivityLevel.isNotBlank()) {
                            binding.spinnerActivity.setText(selectedActivityLevel, false)
                            binding.txtActivityDesc.text =
                                UserConstants.ACTIVITY_LEVELS[selectedActivityLevel]
                        }

                        // Health & Dietary
                        selectedDiets.clear()
                        selectedDiets.addAll(it.dietary)
                        refreshChips(binding.chipGroupDiet, selectedDiets)

                        selectedAllergies.clear()
                        selectedAllergies.addAll(it.allergies)
                        refreshChips(binding.chipGroupAllergies, selectedAllergies)

                        selectedConditions.clear()
                        selectedConditions.addAll(it.conditions)
                        refreshChips(binding.chipGroupConditions, selectedConditions)

                        binding.txtDebugHash.text = "Context Hash: ${it.hashCode()}"
                        if (!it.isComplete()) {
                            binding.txtDebugHash.append("\n⚠️ PROFILE INCOMPLETE FOR AI SYNC")
                        }
                    }
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
}
