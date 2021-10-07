package com.ezzy.missingpersontracker.data.remote

import com.ezzy.core.data.datasource.ChatDataSource
import com.ezzy.core.data.resource.Resource
import com.ezzy.core.domain.Chat
import com.ezzy.core.domain.ChatMessage
import com.ezzy.core.domain.User
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.CHATS
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.MESSAGES
import com.ezzy.missingpersontracker.util.Constants.FIRESTORECOLLECTIONS.USER_COLLECTION
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class ChatRepoImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : ChatDataSource {

    private val chatCollection = firebaseFirestore.collection(CHATS)
    private val userCollection = firebaseFirestore.collection(USER_COLLECTION)

    override suspend fun addChat(userId: String, chat: Chat): Flow<Resource<String>> = flow {
        emit(Resource.loading())
        val chatSnapshot = chatCollection.document(userId)
            .collection(CHATS)
            .add(chat).await()
        emit(Resource.success(chatSnapshot.id))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    private suspend fun saveChatMessage(
        userId: String,
        chatMessage: ChatMessage
    ): Flow<Resource<String>> =
        flow {
            emit(Resource.loading())
            val messageSnapshot = chatCollection.document(userId)
                .collection(MESSAGES)
//                .document(chatId)
//                .collection(MESSAGES)
                .add(chatMessage).await()
            emit(Resource.success(messageSnapshot.id))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)

    override suspend fun sendMessage(
        userId: String,
        chatId: String,
        chatMessage: ChatMessage
    ): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading())
        chatCollection.document(userId)
            .collection(CHATS)
            .document(chatId)
            .collection(MESSAGES)
            .add(chatMessage)
            .await()
        emit(Resource.success(true))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    override suspend fun getChatMessages(
        userId: String,
        chatId: String
    ): Flow<Resource<Pair<User, List<ChatMessage>>>> =
        flow {
            emit(Resource.loading())
            val chatMessages = mutableListOf<ChatMessage>()
            var sender: User? = null
            var chat: Chat? = null
            val chatsSnapshot = chatCollection.document(userId)
                .collection(CHATS)
                .document(chatId)
                .collection(MESSAGES)
                .get().await()

            val chatSnapshot = chatCollection.document(userId)
                .collection(CHATS)
                .document(chatId)
                .get().await()

            chat = chatSnapshot.toObject(Chat::class.java)

            chatsSnapshot.forEach { snapshot ->
                chatMessages.add(snapshot.toObject(ChatMessage::class.java))
            }

            getChatSender(chat?.userId!!).collect { state ->
                when(state) {
                    is Resource.Loading -> Timber.d("message sender loading...")
                    is Resource.Success -> sender = state.data
                    is Resource.Failure -> Timber.e("Loading message sender failed!!")
                }
            }

            emit(Resource.success(Pair(sender!!, chatMessages)))
        }.catch { emit(Resource.failed(it.message.toString())) }
            .flowOn(Dispatchers.IO)


    private fun getChatSender(senderId: String): Flow<Resource<User>> = flow {
        emit(Resource.loading())
        val userSnapshot = userCollection.document(senderId)
            .get().await()
        emit(Resource.success(userSnapshot.toObject(User::class.java)!!))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    override suspend fun getChats(userId: String): Flow<Resource<List<Chat>>> = flow {
        emit(Resource.loading())
        val chats = mutableListOf<Chat>()
        val chatSnapshot = chatCollection.document(userId)
            .collection(CHATS)
            .get().await()

        for (snapshot in chatSnapshot) {
            chats.add(snapshot.toObject(Chat::class.java))
        }

        emit(Resource.success(chats))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)

    override suspend fun deleteChat(userId: String, chatId: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.loading())
        chatCollection.document(userId)
            .collection(CHATS)
            .document(chatId)
            .delete().await()
        emit(Resource.success(true))
    }.catch { emit(Resource.failed(it.message.toString())) }
        .flowOn(Dispatchers.IO)
}