package com.bitewise.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.bitewise.app.ui.onboarding.OnboardingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        // --- DEVELOPMENT HELPERS ---
        // To RESET onboarding (force it to show):
        // 1. Uncomment the line below.
        // 2. Run the app once.
        // 3. Comment the line back out to stop the loop.

//         OnboardingActivity.resetOnboarding(this)

        // To SKIP onboarding (mark as done instantly):
        // OnboardingActivity.setOnboardingDone(this)
        // ---------------------------

        if (!OnboardingActivity.onboardingAlreadyDone(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish() 
            return
        }

        setContentView(R.layout.activity_main)
        setupMainNav()
    }

    private fun setupMainNav() {
        val rootLayout = findViewById<android.view.View>(R.id.main)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(left = systemBars.left, top = systemBars.top, right = systemBars.right)
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(bottomNav) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(bottom = systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_container) as NavHostFragment
        val navController = navHostFragment.navController
        
        bottomNav.setupWithNavController(navController)
    }
}
