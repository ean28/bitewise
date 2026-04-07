package com.bitewise.app.ui.onboarding.basefragment

import android.os.Bundle
import android.view.View
import com.bitewise.app.databinding.FragmentOnboardingBaseBinding
import com.bitewise.app.ui.common.BaseFragment

class OnboardingBaseFragment : BaseFragment<FragmentOnboardingBaseBinding>(
    FragmentOnboardingBaseBinding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // NavHostFragment in XML handles the fragment transactions
    }
}
