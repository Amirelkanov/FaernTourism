package com.example.faerntourism.di

import android.content.Context
import com.example.faerntourism.data.CacheInterceptor
import com.example.faerntourism.network.TourApiService
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
    @Provides
    @Singleton
    fun provideCacheInterceptor() = CacheInterceptor()

    @Provides
    @Singleton
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

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val baseUrl = "https://trip-kavkaz.com/"
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(ScalarsConverterFactory.create()).client(client).build()
    }

    @Provides
    @Singleton
    fun provideTourApiService(retrofit: Retrofit): TourApiService {
        return retrofit.create(TourApiService::class.java)
    }
}
