package com.ezzy.core.data.repository

import com.ezzy.core.data.datasource.ChatDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import kotlinx.coroutines.flow.Flow

class ChatRepository(private val dataSource: ChatDataSource) {
    suspend fun addChat(
        userId: String,
        chat: Chat,
    ): Flow<Resource<String>> = dataSource.addChat(userId, chat)

    suspend fun sendMessage(userId: String, chatId: String,chatMessage: ChatMessage): Flow<Resource<Boolean>> =
        dataSource.sendMessage(userId, chatId,chatMessage)

    suspend fun getChatMessages(userId: String, chatId: String): Flow<Resource<Pair<User, List<ChatMessage>>>> =
        dataSource.getChatMessages(userId, chatId)

    suspend fun getChats(userId: String): Flow<Resource<List<Chat>>> = dataSource.getChats(userId)

    suspend fun deleteChat(userId: String, chatId: String): Flow<Resource<Boolean>> =
        dataSource.deleteChat(userId, chatId)
}