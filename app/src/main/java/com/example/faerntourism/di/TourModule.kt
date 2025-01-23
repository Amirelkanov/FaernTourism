package com.example.faerntourism.di

import com.example.faerntourism.data.TourRepository
import com.example.faerntourism.data.TourRepositoryImpl
import com.example.faerntourism.data.WebScraper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object TourModule {
    
    @Provides
    fun provideTourRepository(scraper: WebScraper): TourRepository = TourRepositoryImpl(scraper)
}
