package co.uk.marketanalyser.feature.marketnews.data.repository

import co.uk.marketanalyser.core.database.dao.MarketNewsDao
import co.uk.marketanalyser.core.network.api.MarketNewsApi
import co.uk.marketanalyser.feature.marketnews.data.mapper.toDomainModel
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsArticle
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsTickerSentiment
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsTopic
import co.uk.marketanalyser.feature.marketnews.domain.repository.MarketNewsRepository
import javax.inject.Inject

/**
 * Implementation of [MarketNewsRepository] that fetches from the Alpha Vantage NEWS_SENTIMENT API.
 */
class MarketNewsRepositoryImpl @Inject constructor(
    private val marketNewsDao: MarketNewsDao,
    private val marketNewsApi: MarketNewsApi
) : MarketNewsRepository {

    companion object {
        /** Cache timeout duration in milliseconds (15 minutes). */
        const val CACHE_TIMEOUT = 15 * 60 * 1000L
    }

    /**
     * Fetches and maps market news articles for the given ticker(s).
     * The raw time string "20260410T143100" is formatted to "Apr 10, 2026 14:31".
     */
    override suspend fun getMarketNews(tickers: String): Result<List<NewsArticle>> {
        val cachedMarketNews = marketNewsDao.getMarketNews()
        val isCacheValid =
            cachedMarketNews.isNotEmpty() && System.currentTimeMillis() - cachedMarketNews.first().cachedAt < CACHE_TIMEOUT
        if (isCacheValid) {
            return Result.success(cachedMarketNews.map { it.toDomainModel() })
        }
        return try {
            val articles = marketNewsApi.getMarketNews(tickers = tickers).feed.map { item ->
                NewsArticle(
                    title = item.title,
                    url = item.url,
                    summary = item.summary,
                    source = item.source,
                    timePublished = formatTime(item.timePublished),
                    overallSentimentLabel = item.overallSentimentLabel,
                    overallSentimentScore = item.overallSentimentScore,
                    topics = item.topics.map {
                        NewsTopic(
                            topic = it.topic,
                            relevanceScore = it.relevanceScore
                        )
                    },
                    tickers = item.tickerSentiment.map {
                        NewsTickerSentiment(
                            ticker = it.ticker,
                            relevanceScore = it.relevanceScore,
                            tickerSentimentScore = it.tickerSentimentScore,
                            tickerSentimentLabel = it.tickerSentimentLabel
                        )
                    }
                )
            }
            Result.success(articles)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Converts "20260410T143100" → "Apr 10, 2026 14:31".
     */
    private fun formatTime(raw: String): String {
        return try {
            // Format: YYYYMMDDTHHmmss
            val year = raw.substring(0, 4)
            val month = raw.substring(4, 6).toInt()
            val day = raw.substring(6, 8)
            val hour = raw.substring(9, 11)
            val minute = raw.substring(11, 13)
            val monthName = listOf(
                "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
            )[month - 1]
            "$monthName $day, $year $hour:$minute"
        } catch (_: Exception) {
            raw
        }
    }
}

