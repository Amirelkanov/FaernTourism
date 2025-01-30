package com.amel.faerntourism.di.ruStore

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object UpdateModule {

    @Provides
    @Singleton
    fun provideUpdateManager(@ApplicationContext context: Context) =
        RuStoreAppUpdateManagerFactory.create(context)
}