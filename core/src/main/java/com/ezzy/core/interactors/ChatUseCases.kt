package com.ezzy.core.interactors

import com.ezzy.core.data.repository.ChatRepository
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import kotlinx.coroutines.flow.Flow

class AddChat(private val repository: ChatRepository) {
    suspend operator fun invoke(
        userId: String,
        chat: Chat,
    ): Flow<Resource<String>> = repository.addChat(userId, chat)
}

class SendMessage(private val repository: ChatRepository) {
    suspend operator fun invoke(
        userId: String, chatId: String,chatMessage: ChatMessage
    ): Flow<Resource<Boolean>> = repository.sendMessage(userId, chatId,chatMessage)
}

class GetChatMessages(private val repository: ChatRepository) {
    suspend operator fun invoke(
        userId: String,
        chatId: String
    ): Flow<Resource<List<ChatMessage>>> = repository.getChatMessages(userId, chatId)
}

class GetChats(private val repository: ChatRepository) {
    suspend operator fun invoke(
        userId: String
    ) = repository.getChats(userId)
}

class DeleteChat(private val repository: ChatRepository) {
    suspend operator fun invoke(
        userId: String,
        chatId: String
    ): Flow<Resource<Boolean>> = repository.deleteChat(userId, chatId)
}

class GetChatId(private val repository: ChatRepository) {
    suspend operator fun invoke(
        userId: String
    ): Flow<Resource<String>> = repository.getChatId(userId)
}