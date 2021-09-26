package com.ezzy.missingpersontracker.di

import android.content.Context
import com.ezzy.missingpersontracker.repository.PreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providePreferencesRepo(
        @ApplicationContext context: Context
    ) = PreferencesRepository(context)

}