package com.example.faerntourism.di

import com.example.faerntourism.data.TourRepository
import com.example.faerntourism.data.TourRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class TourRepositoryModule {

    @Binds
    abstract fun bindTourRepository(
        tourRepositoryImpl: TourRepositoryImpl,
    ): TourRepository
}