package co.uk.marketanalyser.feature.marketnews.data.repository

import co.uk.marketanalyser.core.database.dao.MarketNewsDao
import co.uk.marketanalyser.core.network.api.MarketNewsApi
import co.uk.marketanalyser.feature.marketnews.data.mapper.toDomainModel
import co.uk.marketanalyser.feature.marketnews.data.mapper.toEntity
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsArticle
import co.uk.marketanalyser.feature.marketnews.domain.repository.MarketNewsRepository
import kotlinx.coroutines.CancellationException
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
     *
     * This method follows an offline-first strategy:
     * 1. Check local database for cached news articles.
     * 2. If data exists and is fresh (< 15 minutes), return it immediately.
     * 3. If data is stale or missing, attempt to fetch fresh news from the API.
     * 4. On network success, format timestamps, update the local cache, and return results.
     * 5. On network failure, return the stale cache as a fallback if it exists.
     *
     * Note: [CancellationException] is rethrown to ensure proper coroutine cancellation.
     *
     * @param tickers A ticker symbol or comma-separated list (e.g., "AAPL" or "AAPL,MSFT").
     * @return A [Result] containing a list of [NewsArticle] domain models or an error.
     */
    override suspend fun getMarketNews(tickers: String): Result<List<NewsArticle>> {
        val cachedMarketNews = marketNewsDao.getMarketNews()
        val isCacheValid =
            cachedMarketNews.isNotEmpty() && System.currentTimeMillis() - cachedMarketNews.first().cachedAt < CACHE_TIMEOUT
        
        if (isCacheValid) {
            return Result.success(cachedMarketNews.map { it.toDomainModel() })
        }

        return try {
            val response = marketNewsApi.getMarketNews(tickers = tickers)
            val dtos = response.feed
            
            // Format time and map to domain model for immediate return
            // Also map to entities for caching
            val entities = dtos.map { it.toEntity().copy(timePublished = formatTime(it.timePublished)) }
            
            // Update cache
            entities.forEach { marketNewsDao.insert(it) }
            
            Result.success(entities.map { it.toDomainModel() })
        } catch (e: Exception) {
            if (e is CancellationException) throw e // Rethrow to ensure proper coroutine cancellation
            
            if (cachedMarketNews.isNotEmpty()) {
                Result.success(cachedMarketNews.map { it.toDomainModel() })
            } else {
                Result.failure(e)
            }
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

