package com.ezzy.missingpersontracker.di

import com.ezzy.core.data.repository.MissingPersonImageRepository
import com.ezzy.core.data.repository.MissingPersonRepository
import com.ezzy.core.data.repository.UserRepository
import com.ezzy.core.interactors.*
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

    @Provides
    @Singleton
    fun provideUserRepository(
        firebaseFirestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) = UserRepository(UserRepoImpl(firebaseFirestore, firebaseAuth))

    @Provides
    @Singleton
    fun provideMissingPersonRepo(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ) = MissingPersonRepository(MissingPersonRepoImpl(firebaseFirestore, firebaseStorage))

    @Provides
    @Singleton
    fun provideMissingPersonImagesRepo(
        firebaseFirestore: FirebaseFirestore,
        firebaseStorage: FirebaseStorage
    ) = MissingPersonImageRepository(MissingPersonRepoImpl(firebaseFirestore, firebaseStorage))


    @Provides
    @Singleton
    fun provideRegisterUsers(repository: UserRepository) = RegisterUser(repository)

    @Provides
    @Singleton
    fun provideLoginUser(repository: UserRepository) = LoginUser(repository)

    @Provides
    @Singleton
    fun provideAddMissingPerson(repository: MissingPersonRepository) =
        AddMissingPerson(repository)

    @Provides
    @Singleton
    fun provideAddUser(repository: UserRepository) = AddUser(repository)

    @Provides
    @Singleton
    fun provideCheckUser(repository: UserRepository) = CheckUser(repository)

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

}