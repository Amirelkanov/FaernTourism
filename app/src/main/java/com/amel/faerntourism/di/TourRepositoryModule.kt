package com.amel.faerntourism.di

import com.amel.faerntourism.data.TourRepository
import com.amel.faerntourism.data.TourRepositoryImpl
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