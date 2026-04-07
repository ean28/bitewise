package com.bitewise.app.ui.onboarding.basefragment.health

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bitewise.app.MainActivity
import com.bitewise.app.databinding.Onboarding3Binding
import com.bitewise.app.ui.common.BaseFragment
import com.bitewise.app.ui.onboarding.OnboardingActivity

class Onboarding3Fragment : BaseFragment<Onboarding3Binding>(
    Onboarding3Binding::inflate
) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            activity?.let { 
                OnboardingActivity.setOnboardingDone(it)
                
                val intent = Intent(it, MainActivity::class.java)
                startActivity(intent)
                
                // Close OnboardingActivity
                it.finish()
            }
        }
    }
}
