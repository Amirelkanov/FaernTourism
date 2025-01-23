package com.example.faerntourism.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject

class WebScraper @Inject constructor(
    private val client: HttpClient
) {
    suspend fun fetchHtml(url: String): Document? {
        return try {
            val response: HttpResponse = client.get(url)
            val htmlContent: String = response.body()
            Jsoup.parse(htmlContent)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
