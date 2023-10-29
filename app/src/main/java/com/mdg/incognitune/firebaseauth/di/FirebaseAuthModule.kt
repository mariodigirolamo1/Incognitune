package com.mdg.incognitune.firebaseauth.di

import com.google.firebase.auth.FirebaseAuth
import com.mdg.incognitune.firebaseauth.domain.FirebaseAuthRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface FirebaseAuthModule {

    @Binds
    fun bindFirebaseAuthRepo(
        firebaseAuthRepo: FirebaseAuthRepo
    ): FirebaseAuthRepo
}