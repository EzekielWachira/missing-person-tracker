package com.ezzy.missingpersontracker.ui.fragments.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import com.ezzy.core.interactors.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val addChat: AddChat,
    private val sendMessage: SendMessage,
    private val getChatMessages: GetChatMessages,
    private val getChats: GetChats,
    private val deleteChat: DeleteChat,
    private val checkUser: CheckUser,
    private val getAuthenticatedUserID: GetAuthenticatedUserID
) : ViewModel() {

    private var _chatMessages =
        MutableStateFlow<Resource<Pair<User, List<ChatMessage>>>>(Resource.Empty)
    private var _chats = MutableStateFlow<Resource<List<Chat>>>(Resource.Empty)
    private var _chatId = MutableStateFlow<Resource<String>>(Resource.Empty)
    private var _chatMessageId = MutableStateFlow<Resource<String>>(Resource.Empty)
    private var _isMessageSent = MutableStateFlow<Resource<Boolean>>(Resource.Empty)
    private var _isChatDeleted = MutableStateFlow<Resource<Boolean>>(Resource.Empty)
    private var _authUserId = MutableStateFlow<Resource<String>>(Resource.Empty)
    private var _user = MutableStateFlow<Resource<User>>(Resource.Empty)
    val user: StateFlow<Resource<User>> get() = _user
    val userId: StateFlow<Resource<String>> get() = _authUserId
    val chatMessages: StateFlow<Resource<Pair<User, List<ChatMessage>>>> get() = _chatMessages
    val chats: StateFlow<Resource<List<Chat>>> get() = _chats
    val chatId: StateFlow<Resource<String>> get() = _chatId
    val chatMessageId: StateFlow<Resource<String>> get() = _chatMessageId
    val isMessageSent: StateFlow<Resource<Boolean>> get() = _isMessageSent
    val isChatDeleted: StateFlow<Resource<Boolean>> get() = _isChatDeleted

    fun chatAdd(
        userId: String,
        senderId: String,
        chat: Chat,
        chatMessage: ChatMessage
    ) = viewModelScope.launch {
        addChat(userId, senderId, chat, chatMessage).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> Resource.loading<String>()
                is Resource.Success -> _chatId.value = Resource.success(resourceState.data)
                is Resource.Failure -> _chatId.value = Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun sendAMessage(
        userId: String,
        chatId: String,
        chatMessage: ChatMessage
    ) = viewModelScope.launch {
        sendMessage(userId, chatId, chatMessage).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> Resource.loading<Boolean>()
                is Resource.Success -> _isMessageSent.value = Resource.success(resourceState.data)
                is Resource.Failure -> _isMessageSent.value =
                    Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun getAChatMessages(
        userId: String,
        chatId: String
    ) = viewModelScope.launch {
        getChatMessages(userId, chatId).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> Resource.loading<Pair<User, List<ChatMessage>>>()
                is Resource.Success -> _chatMessages.value = Resource.success(resourceState.data)
                is Resource.Failure -> _chatMessages.value =
                    Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun getAllChats(userId: String) = viewModelScope.launch {
        getChats(userId).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> Resource.loading<List<Chat>>()
                is Resource.Success -> _chats.value = Resource.success(resourceState.data)
                is Resource.Failure -> _chats.value =
                    Resource.failed(resourceState.errorMessage!!)
                is Resource.Empty -> _chats.value = Resource.Empty
            }
        }
    }

    fun deleteAChat(userId: String, chatId: String) = viewModelScope.launch {
        deleteChat(userId, chatId).collect { resourceState ->
            when (resourceState) {
                is Resource.Loading -> Resource.loading<Boolean>()
                is Resource.Success -> _isChatDeleted.value = Resource.success(resourceState.data)
                is Resource.Failure -> _isChatDeleted.value =
                    Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun getAuthUserId(email: String?, phoneNumber: String?)  = viewModelScope.launch {
        getAuthenticatedUserID(email, phoneNumber).collect { resourceState ->
            when(resourceState) {
                is Resource.Loading -> _authUserId.value = Resource.loading()
                is Resource.Success -> {
                    Timber.d("USERID: ${resourceState.data}")
                    _authUserId.value = Resource.success(resourceState.data)
                }
                is Resource.Failure -> _authUserId.value = Resource.failed(resourceState.errorMessage!!)
            }
        }
    }

    fun checkAuthenticatedUser(email: String?, phoneNumber: String?) =
        viewModelScope.launch {
            checkUser(email, phoneNumber).collect { state ->
                when(state) {
                    is Resource.Loading -> {
                        _user.value = Resource.loading()
                    }
                    is Resource.Success -> {
                        _user.value = Resource.success(state.data)
                    }
                    is Resource.Failure -> {
                        Timber.e(state.errorMessage)
//                        _user = Resource.failed("")
                    }
                }
            }
        }


}