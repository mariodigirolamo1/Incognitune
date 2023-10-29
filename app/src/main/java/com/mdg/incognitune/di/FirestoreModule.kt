package com.mdg.incognitune.di

import com.mdg.incognitune.data.Datasource
import com.mdg.incognitune.data.FirestoreDatasource
import com.mdg.incognitune.data.FirestoreRepository
import dagger.Binds
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