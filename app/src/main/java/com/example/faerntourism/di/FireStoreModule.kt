package com.example.faerntourism.di

import com.example.faerntourism.data.FireStoreRepository
import com.example.faerntourism.data.FireStoreRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Provides
    fun provideFireStore(): FirebaseFirestore = Firebase.firestore

    @Provides
    fun providesFireStoreRepository(impl: FireStoreRepositoryImpl): FireStoreRepository = impl
}
