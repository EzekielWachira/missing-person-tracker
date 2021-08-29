package com.ezzy.missingpersontracker.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.databinding.ActivityMainBinding
import com.ezzy.missingpersontracker.ui.activities.person_details.PersonDetailsActivity
import com.ezzy.missingpersontracker.ui.activities.search.SearchMissingPersonActivity
import com.ezzy.missingpersontracker.ui.activities.user_details.UserDetailsActivity
import com.ezzy.missingpersontracker.ui.fragments.auth.LoginActivity
import com.ezzy.missingpersontracker.util.showToast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()
    private var user: User? = null

    @Inject
    lateinit var authUI: AuthUI
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navController = this.findNavController(R.id.mainNavHostContainer)
        binding.bottomNavigation.setupWithNavController(navController)

        checkAuthenticatedUser()
        subscribeListeners()
        setUpUI()
    }

    private fun checkAuthenticatedUser() {
        with(firebaseAuth.currentUser) {
            when {
                this?.email?.isNotEmpty() == true -> {
                    mainViewModel.checkAuthenticatedUser(this.email, null)
                }
                this?.phoneNumber?.isNotEmpty() == true -> {
                    mainViewModel.checkAuthenticatedUser(null, this.phoneNumber)
                }
                else -> return@with
            }
        }
    }

    private fun setUpUI() {
        binding.fab.setOnClickListener {
            when (user) {
                null -> {
                    showToast("Before reporting, we need your full details")
                    startActivity(
                        Intent(this, UserDetailsActivity::class.java)
                    )
                }
                else -> {
                    startActivity(
                        Intent(
                            this,
                            ReportMissingPersonActivity::class.java
                        )
                    )
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_search -> startActivity(
                Intent(
                    this,
                    SearchMissingPersonActivity::class.java
                )
            )
            R.id.action_logout -> authUI.signOut(this)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(
                            Intent(
                                this, LoginActivity::class.java
                            )
                        )
                        finish()
                    }
                }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun subscribeListeners() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.user.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        showToast("Checking your details")
                    }
                    is Resource.Success -> {
                        user = state.data
                    }
                    is Resource.Failure -> {
                        showToast("Could not check your details")
                    }
                }
            }
        }
    }
}