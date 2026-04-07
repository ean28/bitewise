package com.bitewise.app.ui.onboarding.basefragment.lifestyle

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bitewise.app.R
import com.bitewise.app.databinding.Onboarding2Binding
import com.bitewise.app.ui.common.BaseFragment

class Onboarding2Fragment : BaseFragment<Onboarding2Binding>(
    Onboarding2Binding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_onboarding2_to_onboarding3)
        }
    }
}
