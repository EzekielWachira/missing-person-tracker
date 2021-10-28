package com.ezzy.core.data.datasource

import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import kotlinx.coroutines.flow.Flow

interface ChatDataSource {
    suspend fun addChat(
        userId: String,
        chat: Chat,
    ): Flow<Resource<String>>

    suspend fun sendMessage(userId: String, chatId: String, chatMessage: ChatMessage): Flow<Resource<Boolean>>

    suspend fun getChatMessages(userId: String, chatId: String): Flow<Resource<List<ChatMessage>>>

    suspend fun getChats(userId: String): Flow<Resource<List<Chat>>>

    suspend fun deleteChat(userId: String, chatId: String): Flow<Resource<Boolean>>

    suspend fun getChatId(userId: String): Flow<Resource<String>>
}