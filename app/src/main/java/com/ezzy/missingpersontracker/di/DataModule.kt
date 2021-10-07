package com.ezzy.missingpersontracker.di

import com.ezzy.core.data.repository.ChatRepository
import com.ezzy.core.data.repository.MissingPersonImageRepository
import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.data.repository.UserRepository
import com.ezzy.core.interactors.*
import com.ezzy.missingpersontracker.data.remote.ChatRepoImpl
import com.ezzy.missingpersontracker.data.remote.MissingPersonRepoImpl
import com.ezzy.missingpersontracker.data.remote.UserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    /**
    * this function provides user repository as a dependency that can later be injected
     * in any file in the application source code
    * */
    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) = UserRepository(UserRepoImpl(firebaseFirestore, firebaseAuth))

    /**
     * this function provides missing person repository as a dependency that can later be injected
     * in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideMissingPersonRepo(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ) = MissingPersonRepository(MissingPersonRepoImpl(firebaseFirestore, firebaseStorage))

    /**
     * this function provides missing person image repository as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideMissingPersonImagesRepo(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ) = MissingPersonImageRepository(MissingPersonRepoImpl(firebaseFirestore, firebaseStorage))

    /**
     * this function provides missing chat repository as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideChatRepo(
        firebaseFirestore: FirebaseFirestore
    ) = ChatRepository(ChatRepoImpl(firebaseFirestore))


    /**
     * this function provides register user usecase as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideRegisterUsers(repository: UserRepository) = RegisterUser(repository)

    /**
     * this function provides login user usecase as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideLoginUser(repository: UserRepository) = LoginUser(repository)

    /**
     * this function provides add missing person usecase as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideAddMissingPerson(repository: MissingPersonRepository) =
        AddMissingPerson(repository)

    /**
     * this function provides add user usecase as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideAddUser(repository: UserRepository) = AddUser(repository)

    @Provides
    @Singleton
    fun provideGetReporterId(repository: UserRepository) = GetReporterId(repository)

    /**
     * this function provides check user usecase as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideCheckUser(repository: UserRepository) = CheckUser(repository)

    @Provides
    @Singleton
    fun provideGetUsers(repository: UserRepository) = GetAllUsers(repository)

    @Provides
    @Singleton
    fun provideSearchUser(repository: UserRepository) = SearchUser(repository)

    /**
     * this function provides get auth user id usecase as a dependency that can later be
     * injected in any file in the application source code
     * */
    @Provides
    @Singleton
    fun provideGetAuthUserID(repository: UserRepository) = GetAuthenticatedUserID(repository)

    @Provides
    @Singleton
    fun provideGetMissingPeople(repository: MissingPersonRepository) = GetMissingPeople(repository)

    @Provides
    @Singleton
    fun provideGetFoundPeople(repository: MissingPersonRepository) =
        GetFoundPeople(repository)

    @Provides
    @Singleton
    fun provideGetMissingPersonImages(repository: MissingPersonImageRepository) =
        GetPersonImages(repository)

    @Provides
    @Singleton
    fun provideGetMissingPersonReporter(repository: UserRepository) =
        GetMissingPersonReporter(repository)

    @Singleton
    @Provides
    fun provideAllGetMissingPersonImages(repository: MissingPersonRepository) =
        GetMissingPersonImages(repository)

    @Provides
    @Singleton
    fun provideReportFoundPerson(repository: MissingPersonRepository) =
        ReportFoundPerson(repository)

    @Provides
    @Singleton
    fun provideAddChat(repository: ChatRepository) = AddChat(repository)

    @Provides
    @Singleton
    fun provideSendMessage(repository: ChatRepository) = SendMessage(repository)

    @Provides
    @Singleton
    fun provideGetChatMessages(repository: ChatRepository) =
        GetChatMessages(repository)

    @Provides
    @Singleton
    fun provideGetChats(repository: ChatRepository) = GetChats(repository)

    @Provides
    @Singleton
    fun provideDeleteChat(repository: ChatRepository) = DeleteChat(repository)

    @Provides
    @Singleton
    fun provideSearchMissingPerson(repository: MissingPersonRepository) =
        SearchMissingPerson(repository)

    @Provides
    @Singleton
    fun provideSearchMissingPersonByFirstName(repository: MissingPersonRepository) =
        SearchMissingPersonByFirstName(repository)

}