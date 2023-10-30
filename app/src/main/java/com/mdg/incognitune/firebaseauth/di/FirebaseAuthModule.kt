package com.mdg.incognitune.firebaseauth.di

import com.mdg.incognitune.firebaseauth.domain.FirebaseAuthRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseAuthModule {

    @Singleton
    @Provides
    fun provideFirebaseAuthRepo(): FirebaseAuthRepo = FirebaseAuthRepo()
}