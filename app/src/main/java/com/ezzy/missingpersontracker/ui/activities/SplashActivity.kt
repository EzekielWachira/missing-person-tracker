package com.ezzy.missingpersontracker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivitySplashBinding
import com.ezzy.missingpersontracker.ui.fragments.auth.AuthViewModel
import com.ezzy.missingpersontracker.ui.fragments.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding

    private val authViewModel: AuthViewModel by viewModels()

    private var isUserLoggedIn: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val animation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
        binding.splashImage.animation = animation

        authViewModel.loginStatus.observe(this) { isUserLoggedIn = it }

        Handler(Looper.getMainLooper()).postDelayed({

            if (isUserLoggedIn!!) {
                startActivity(
                    Intent(
                        this, MainActivity::class.java
                    )
                )
                finish()
            } else {
                startActivity(
                    Intent(
                        this, LoginActivity::class.java
                    )
                )
                finish()
            }

        }, 3000)
    }
}