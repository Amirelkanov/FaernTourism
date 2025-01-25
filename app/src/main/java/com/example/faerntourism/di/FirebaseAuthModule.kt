package com.example.faerntourism.di

import com.example.faerntourism.data.FirebaseAuthRepository
import com.example.faerntourism.data.FirebaseAuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
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
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Singleton
    @Provides
    fun providesAuthRepository(impl: FirebaseAuthRepositoryImpl): FirebaseAuthRepository = impl
}
