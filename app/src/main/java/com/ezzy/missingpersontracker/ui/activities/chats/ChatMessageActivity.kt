package com.ezzy.missingpersontracker.ui.activities.chats

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.common.CommonAdapter
import com.ezzy.missingpersontracker.common.Directions
import com.ezzy.missingpersontracker.common.ItemDecorator
import com.ezzy.missingpersontracker.databinding.ActivityChatMessageBinding
import com.ezzy.missingpersontracker.ui.fragments.chats.ChatMessageViewHolder
import com.ezzy.missingpersontracker.util.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ChatMessageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatMessageBinding
    private var user: User? = null
    private var userId: String? = null
    private var reporterId: String? = null
    private var chatId: String? = null

    private val chatMainViewModel: ChatMainViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth
    private lateinit var progressDialog: SweetAlertDialog
    private lateinit var chatMessageAdapter: CommonAdapter<ChatMessage>

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

        progressDialog = showLoadingDialog("Sending message...")

        initListeners()
        setUpRecyclerview()
        subscribeToUI()
        setUpUI()

        chatId?.let {
            chatMainViewModel.getAChatMessages(userId!!, it)
        }
    }

    private fun setUpUI() {
        with(binding) {
            sendBtn.setOnClickListener {
                if (message.isEmpty()) {
                    showToast("Message cannot be empty")
                } else {
                    when {
                        userId == null -> showToast("Waiting for user id...")
                        reporterId == null -> showToast("Waiting for reporter id...")
                        else -> {
                            val chatMessage = ChatMessage(
                                message.text.toString(),
                                System.currentTimeMillis(),
                                false
                            )
                            val chat = Chat(userId)
                            chatMainViewModel.chatAdd(userId!!, chat)
                            lifecycleScope.launch {
                                with(chatMainViewModel) {
                                    chatId.collect { state ->
                                        when (state) {
                                            is Resource.Loading -> showToast("Loading chat id...")
                                            is Resource.Success -> {
                                                sendAMessage(
                                                    this@ChatMessageActivity.userId!!,
                                                    state.data,
                                                    chatMessage
                                                )
                                            }
                                            is Resource.Failure -> showToast("Error loading chat id")
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun setUpRecyclerview() {
        chatMessageAdapter = CommonAdapter {
            ChatMessageViewHolder(it, this)
        }
        binding.messageRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatMessageActivity)
            adapter = chatMessageAdapter
            addItemDecoration(ItemDecorator(Directions.VERTICAL, 5))
        }
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
                when (state) {
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
                when (state) {
                    is Resource.Loading -> showToast("Loading userId")
                    is Resource.Success -> {
                        userId = state.data
                        showToast("User ID ${state.data}")
                    }
                    is Resource.Failure -> showToast("Error loading user Id")
                }
            }
        }

        lifecycleScope.launch {
            chatMainViewModel.chatId.collect { state ->
                when (state) {
                    is Resource.Loading -> showToast("Loading chatid")
                    is Resource.Success -> {
                        chatId = state.data
                        showToast("chat ID ${state.data}")
                    }
                    is Resource.Failure -> showToast("Error loading chat Id")
                }
            }
        }

        lifecycleScope.launch {
            chatMainViewModel.chatMessages.collect { state->
                when(state) {
                    is Resource.Loading -> binding.spinKit.visible()
                    is Resource.Success -> {
                        binding.spinKit.gone()
                        binding.layoutNoData.gone()
                        Timber.d("CHATSS:>> ${state.data}")
                        chatMessageAdapter.differ.submitList(state.data)
                    }
                    is Resource.Failure -> {
                        binding.spinKit.gone()
                        binding.layoutNoData.gone()
                        showToast("Error getting chat messages")
                    }
                    is Resource.Empty -> {
                        binding.spinKit.gone()
                        binding.layoutNoData.visible()
                    }
                }
            }
        }



    }
}