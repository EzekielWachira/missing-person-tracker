package com.ezzy.missingpersontracker.ui.activities.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.databinding.ActivityChatMessageBinding
import com.ezzy.missingpersontracker.util.showToast
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMessageBinding
    private var user: User? = null
    private var userId: String? = null
    private var reporterId: String? = null

    private val chatMainViewModel: ChatMainViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra("user")) {
            user = intent.getSerializableExtra("user") as User

            user?.let {
                supportActionBar?.apply {
                    title = it.firstName + " " + it.lastName
                    setDisplayHomeAsUpEnabled(true)
                }

                chatMainViewModel.getReporterID(it.email, null)
            }
        }

        initListeners()
        subscribeToUI()
        setUpUI()
    }

    private fun setUpUI() {

    }

    private fun initListeners() {
        lifecycleScope.launchWhenStarted {
            if (firebaseAuth.currentUser?.email?.isNotEmpty()!!) {
                chatMainViewModel.getAuthUserId(firebaseAuth.currentUser!!.email, null)
            } else if (firebaseAuth.currentUser?.phoneNumber?.isNotEmpty()!!) {
                chatMainViewModel.getAuthUserId(null, firebaseAuth.currentUser!!.phoneNumber)
            }
        }
    }

    private fun subscribeToUI() {
        lifecycleScope.launch {
            chatMainViewModel.reporterId.collect { state ->
                when(state) {
                    is Resource.Loading -> showToast("Loading reporterID")
                    is Resource.Success -> {
                        reporterId = state.data
                        showToast("Reporter ID${state.data}")
                    }
                    is Resource.Failure -> showToast("Error loading reporter Id")
                }
            }
        }

        lifecycleScope.launch {
            chatMainViewModel.userId.collect { state ->
                when(state) {
                    is Resource.Loading -> showToast("Loading userId")
                    is Resource.Success -> {
                        userId = state.data
                        showToast("User ID ${state.data}")
                    }
                    is Resource.Failure -> showToast("Error loading user Id")
                }
            }
        }
    }
}