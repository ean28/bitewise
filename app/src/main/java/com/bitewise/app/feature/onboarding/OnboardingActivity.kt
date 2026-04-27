package com.bitewise.app.feature.onboarding

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bitewise.app.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rootContainer.visibility = View.GONE

        binding.btnGetStarted.setOnClickListener {
            binding.rootContainer.visibility = View.VISIBLE
            binding.btnGetStarted.visibility = View.GONE
            binding.tvDescription.visibility = View.GONE
            binding.tvTitle.visibility = View.GONE
            binding.ivHero.visibility = View.GONE
        }
    }
}
