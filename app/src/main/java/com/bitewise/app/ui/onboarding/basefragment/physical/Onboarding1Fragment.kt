package com.bitewise.app.ui.onboarding.basefragment.physical

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bitewise.app.R
import com.bitewise.app.databinding.Onboarding1Binding
import com.bitewise.app.ui.common.BaseFragment

class Onboarding1Fragment : BaseFragment<Onboarding1Binding>(
    Onboarding1Binding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding1_to_onboarding2)
        }
    }
}
