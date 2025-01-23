package com.example.faerntourism.data

import com.example.faerntourism.data.model.Tour
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

interface TourRepository {
    suspend fun getTours(): Result<List<Tour>>
}

const val BASE_URL = "https://trip-kavkaz.com"


class TourRepositoryImpl(
    private val scraper: WebScraper
) : TourRepository {
    override suspend fun getTours(): Result<List<Tour>> {
        return try {
            val document: Document = scraper.fetchHtml("$BASE_URL/sev-osetiya-i-ingushetiya")
                ?: return Result.failure(Exception("Failed to fetch the HTML."))

            val tours = document.selectFirst(".tours-list")?.children()?.map { child ->
                parseTour(child)
            } ?: emptyList()
            Result.success(tours)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseTour(element: Element): Tour {
        val name = element.select("h2.uk-card-title a").text()
        val link = element.select("h2.uk-card-title a").attr("href")
        val price =
            element.select(".uk-text-large.uk-text-primary strong").text().replace("руб.", "₽")
        val date = element.select("div:contains(Ближайшая дата) > span").text()
        val imgLink = element.select("a.tour-image img").attr("src")
        val description =
            element.select(".uk-margin-bottom.uk-visible\\@s").text().replace("О туре", "")

        return Tour(
            name = name,
            price = price,
            link = "$BASE_URL$link",
            date = date,
            imgLink = "$BASE_URL$imgLink",
            description = description
        )
    }
}
