package com.mdg.incognitune.firestore.di

import com.mdg.incognitune.common.data.Datasource
import com.mdg.incognitune.firestore.data.FirestoreDatasource
import com.mdg.incognitune.firestore.data.FirestoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FirestoreModule {

    @Provides
    fun provideFirestoreDatasource(): Datasource = FirestoreDatasource()

    @Provides
    fun provideFirestoreRepository(
        firestoreDatasource: FirestoreDatasource
    ) = FirestoreRepository(firestoreDatasource = firestoreDatasource)
}