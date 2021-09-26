package com.ezzy.missingpersontracker.ui.fragments.chats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.R
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.FragmentChatHomeBinding
import com.ezzy.missingpersontracker.util.gone
import com.ezzy.missingpersontracker.util.showToast
import com.ezzy.missingpersontracker.util.visible
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChatHomeFragment : Fragment() {

    private var _binding: FragmentChatHomeBinding? = null
    private val binding: FragmentChatHomeBinding get() = _binding!!

    private val chatViewModel: ChatViewModel by activityViewModels()

    private lateinit var mAdapter: CommonAdapter<Chat>
    @Inject lateinit var firebaseAuth: FirebaseAuth
    private var user: User? = null
    private var userId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatHomeBinding.inflate(inflater, container, false)

        getAuthUserId()
        getAuthenticatedUser()

        userId?.let {
            chatViewModel.getAllChats(it)
        }

        subscribeToUI()
        setUpRecycleView()

        return binding.root
    }

    private fun setUpRecycleView() {
        with(binding) {
            mAdapter = CommonAdapter { ChatViewHolder(it, requireContext()) }
            chatRecyclerView.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = mAdapter
                addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
            }
        }
    }

    private fun subscribeToUI() {
        lifecycleScope.launch {
            chatViewModel.userId.collect { state ->
                when(state) {
                    is Resource.Loading -> showToast("Checking your details")
                    is Resource.Success -> {
                        userId = state.data
                    }
                    is Resource.Failure -> showToast(state.errorMessage!!)
                }
            }
            chatViewModel.user.collect { state ->
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

        lifecycleScope.launch {
            chatViewModel.chats.collect { state ->
                when(state) {
                    is Resource.Loading -> {
                        binding.spinKit.visible()
                    }
                    is Resource.Success -> {
                        binding.spinKit.gone()
                        mAdapter.differ.submitList(state.data)
                    }
                    is Resource.Failure -> {
                        binding.spinKit.gone()
                        showToast("Could not check your details")
                    }
                    is Resource.Empty -> {
                        binding.layoutNoChats.visible()
                    }
                }
            }
        }
    }

    private fun getAuthUserId() {
        with(firebaseAuth.currentUser) {
            when {
                this?.email?.isNotEmpty() == true -> {
                    Timber.d("EMAIL AVAILABLE: ${this.email}")
                    chatViewModel.getAuthUserId(this.email, null)
                }
                this?.phoneNumber?.isNotEmpty() == true -> {
                    Timber.d("PHONE AVAILABLE: ${this.phoneNumber}")
                    chatViewModel.getAuthUserId(null, this.phoneNumber)
                }
                else -> return@with
            }
        }
    }

    private fun getAuthenticatedUser() {
        with(firebaseAuth.currentUser) {
            when {
                this?.email?.isNotEmpty() == true -> {
                    chatViewModel.checkAuthenticatedUser(this.email, null)
                }
                this?.phoneNumber?.isNotEmpty() == true -> {
                    chatViewModel.checkAuthenticatedUser(null, this.phoneNumber)
                }
                else -> return@with
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}