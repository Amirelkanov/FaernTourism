package com.example.faerntourism.di

import com.example.faerntourism.data.GoogleAuthRepository
import com.example.faerntourism.data.GoogleAuthRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object GoogleAuthModule {

    @Provides
    fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    fun providesAuthRepository(impl: GoogleAuthRepositoryImpl): GoogleAuthRepository = impl
}
