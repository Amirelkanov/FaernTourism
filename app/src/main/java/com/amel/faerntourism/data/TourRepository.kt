package com.amel.faerntourism.data

import com.amel.faerntourism.data.model.Tour
import com.amel.faerntourism.network.TourApiService
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import retrofit2.Response
import javax.inject.Inject

interface TourRepository {
    suspend fun getTours(): Result<List<Tour>>
}

class TourRepositoryImpl @Inject constructor(
    private val tourApiService: TourApiService
) : TourRepository {

    override suspend fun getTours(): Result<List<Tour>> {
        return try {
            val response: Response<ResponseBody> = tourApiService.getToursHtml()

            if (!response.isSuccessful || response.body() == null) {
                return Result.failure(
                    Exception("Network call failed with code: ${response.code()}")
                )
            }

            val document: Document = Jsoup.parse(response.body()!!.string())

            val tours = document.selectFirst(".tours-list")
                ?.children()
                ?.map { element -> parseTour(element) }
                ?: emptyList()

            Result.success(tours)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseTour(element: Element): Tour {
        val name = element.select("h2.uk-card-title a").text()
        val link = element.select("h2.uk-card-title a").attr("href")
        val price = element.select(".uk-text-large.uk-text-primary strong")
            .text()
            .replace("руб.", "₽")
        val date = element.select("div:contains(Ближайшая дата) > span").text()
        val imgLink = element.select("a.tour-image img").attr("src")
        val description = element.select(".uk-margin-bottom.uk-visible\\@s")
            .text()
            .replace("О туре", "")

        return Tour(
            name = name,
            price = price,
            date = date,
            link = "https://trip-kavkaz.com$link",
            imgLink = "https://trip-kavkaz.com$imgLink",
            description = description
        )
    }
}
