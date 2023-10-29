package com.mdg.incognitune.firestore.di

import com.mdg.incognitune.common.data.Datasource
import com.mdg.incognitune.firestore.data.FirestoreDatasource
import com.mdg.incognitune.firestore.data.FirestoreRepository
import com.mdg.incognitune.firestore.domain.AddSongRecordUseCase
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
}