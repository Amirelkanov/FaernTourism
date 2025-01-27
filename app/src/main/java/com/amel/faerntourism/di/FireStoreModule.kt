package com.amel.faerntourism.di

import com.amel.faerntourism.data.FireStoreRepository
import com.amel.faerntourism.data.FireStoreRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Singleton
    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

    @Singleton
    @Provides
    fun providesFireStoreRepository(impl: FireStoreRepositoryImpl): FireStoreRepository = impl
}
