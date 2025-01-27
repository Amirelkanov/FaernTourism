package com.amel.faerntourism.di

import com.amel.faerntourism.data.FirebaseAuthRepository
import com.amel.faerntourism.data.FirebaseAuthRepositoryImpl
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
