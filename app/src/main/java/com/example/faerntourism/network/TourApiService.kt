package com.example.faerntourism.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET

interface TourApiService {
    @GET("sev-osetiya-i-ingushetiya")
    suspend fun getToursHtml(): Response<ResponseBody>
}
