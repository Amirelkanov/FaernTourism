package com.amel.faerntourism.di

import android.content.Context
import com.amel.faerntourism.data.CacheInterceptor
import com.amel.faerntourism.network.TourApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideCacheInterceptor() = CacheInterceptor()

    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        cacheInterceptor: CacheInterceptor,
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(
                Cache(
                    directory = File(context.cacheDir, "http_cache"),
                    maxSize = 10L * 1024L * 1024L
                )
            )
            .addNetworkInterceptor(cacheInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val baseUrl = "https://trip-kavkaz.com/"
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create()).client(client).build()
    }

    @Singleton
    @Provides
    fun provideTourApiService(retrofit: Retrofit): TourApiService {
        return retrofit.create(TourApiService::class.java)
    }
}
