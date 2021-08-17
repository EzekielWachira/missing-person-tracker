package com.ezzy.missingpersontracker.di

import com.ezzy.core.data.repository.UserRepository
import com.ezzy.core.interactors.LoginUser
import com.ezzy.core.interactors.RegisterUser
import com.ezzy.missingpersontracker.data.remote.UserRepoImpl
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
    fun provideRegisterUsers(repository: UserRepository) = RegisterUser(repository)

    @Provides
    @Singleton
    fun provideLoginUser(repository: UserRepository) = LoginUser(repository)

}