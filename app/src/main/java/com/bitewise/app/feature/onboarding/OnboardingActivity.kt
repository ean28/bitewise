package com.bitewise.app.feature.onboarding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
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
    companion object {
        private const val PREFS_NAME = "onboarding_prefs"
        private const val KEY_DONE = "onboarding_done"

        fun onboardingAlreadyDone(context: Context): Boolean {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            return prefs.getBoolean(KEY_DONE, false)
        }

        fun setOnboardingDone(context: Context) {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            prefs.edit { putBoolean(KEY_DONE, true) }
        }

        fun resetOnboarding(context: Context) {
            val prefs = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            prefs.edit { putBoolean(KEY_DONE, false) }
        }
    }
}
