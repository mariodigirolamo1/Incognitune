package com.mdg.incognitune.firestore.di

import com.mdg.incognitune.common.data.Datasource
import com.mdg.incognitune.firebaseauth.data.FirebaseAuthRepo
import com.mdg.incognitune.firestore.data.FirestoreDatasource
import com.mdg.incognitune.firestore.data.FirestoreRepository
import com.mdg.incognitune.firestore.domain.AddSongRecordUseCase
import com.mdg.incognitune.firestore.domain.GetRandomSongUseCase
import com.mdg.incognitune.firestore.domain.GetSongsCountUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Singleton
    @Provides
    fun provideFirestoreDatasource(): Datasource = FirestoreDatasource()

    @Singleton
    @Provides
    fun provideFirestoreRepository(
        firestoreDatasource: FirestoreDatasource
    ) = FirestoreRepository(firestoreDatasource = firestoreDatasource)

    @Singleton
    @Provides
    fun provideAddSongRecordUseCase(
        firestoreRepository: FirestoreRepository
    ) = AddSongRecordUseCase(firestoreRepository = firestoreRepository)

    @Singleton
    @Provides
    fun provideGetSongsCountUseCase(
        firestoreRepository: FirestoreRepository
    ) = GetSongsCountUseCase(firestoreRepository = firestoreRepository)

    @Singleton
    @Provides
    fun provideGetRandomSongUseCase(
        firestoreRepository: FirestoreRepository,
        firebaseAuthRepo: FirebaseAuthRepo
    ) = GetRandomSongUseCase(
        firestoreRepository = firestoreRepository,
        firebaseAuthRepo = firebaseAuthRepo
    )
}