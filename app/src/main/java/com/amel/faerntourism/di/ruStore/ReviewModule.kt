package com.amel.faerntourism.di.ruStore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.rustore.sdk.review.RuStoreReviewManagerFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ReviewModule {

    @Provides
    @Singleton
    fun provideReviewManager(@ApplicationContext context: Context) =
        RuStoreReviewManagerFactory.create(context)
}