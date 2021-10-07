package com.ezzy.missingpersontracker.ui.fragments.chats

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
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

    private lateinit var mAdapter: CommonAdapter<User>

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
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

        chatViewModel.getUsers()
        setUpUI()
        subscribeToUI()
        setUpRecycleView()

        return binding.root
    }

    private fun setUpRecycleView() {
        with(binding) {
            mAdapter = CommonAdapter { ChatListViewHolder(it, requireContext()) }
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
                when (state) {
                    is Resource.Loading -> Timber.d("Checking user details")
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
            chatViewModel.users.collect { state ->
                when (state) {
                    is Resource.Loading -> {
                        binding.spinKit.visible()
                        binding.layoutNoChats.gone()
                    }
                    is Resource.Success -> {
                        binding.spinKit.gone()
                        binding.layoutNoChats.gone()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter.setOnClickListener { user ->
            val bundle = bundleOf("user" to user)
            findNavController().navigate(R.id.action_chatHomeFragment_to_chatFragment, bundle)
        }
    }

    private fun setUpUI() {
        with(binding) {
            search.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (search.text.toString().isNotEmpty()) {
                        chatViewModel.searchUsers(search.text.toString())
                    }
                }

                override fun afterTextChanged(s: Editable?) {
                    if (search.text.toString().isNotEmpty()) {
                        chatViewModel.searchUsers(s.toString())
                    }
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}