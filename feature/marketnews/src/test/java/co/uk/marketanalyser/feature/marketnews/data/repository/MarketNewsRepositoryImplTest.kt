package co.uk.marketanalyser.feature.marketnews.data.repository

import co.uk.marketanalyser.core.database.dao.MarketNewsDao
import co.uk.marketanalyser.core.network.api.MarketNewsApi
import co.uk.marketanalyser.core.network.dto.MarketNewsItemDto
import co.uk.marketanalyser.core.network.dto.MarketNewsResponse
import co.uk.marketanalyser.core.network.dto.TickerSentimentDto
import co.uk.marketanalyser.core.network.dto.TopicDto
import co.uk.marketanalyser.feature.marketnews.data.mapper.toDomainModel
import co.uk.marketanalyser.feature.marketnews.data.mapper.toEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class MarketNewsRepositoryImplTest {

    private val marketNewsApi: MarketNewsApi = mockk()
    private val marketNewsDao: MarketNewsDao = mockk()
    private val repository = MarketNewsRepositoryImpl(marketNewsDao, marketNewsApi)

    private val fakeItem = MarketNewsItemDto(
        title = "Apple leads global smartphone shipments in Q1",
        url = "https://seekingalpha.com/news/4574020",
        timePublished = "20260410T130159",
        authors = listOf("Ravikash Bakolia"),
        summary = "Apple led global smartphone shipments in the first quarter of 2026.",
        bannerImage = null,
        source = "Seeking Alpha",
        sourceDomain = "Seeking Alpha",
        topics = listOf(TopicDto("earnings", "0.935895"), TopicDto("technology", "0.826503")),
        overallSentimentScore = 0.441002,
        overallSentimentLabel = "Bullish",
        tickerSentiment = listOf(
            TickerSentimentDto("AAPL", "1.000000", "0.405618", "Bullish")
        )
    )

    private val fakeEntity = fakeItem.toEntity().copy(timePublished = "Apr 10, 2026 13:01")

    private val fakeResponse = MarketNewsResponse(
        items = "1",
        sentimentScoreDefinition = "",
        relevanceScoreDefinition = "",
        feed = listOf(fakeItem)
    )

    @Test
    fun `getMarketNews returns cached data when fresh`() = runTest {
        // GIVEN: Cache is only 5 minutes old
        val freshCache = listOf(fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (5 * 60 * 1000L)
        ))
        coEvery { marketNewsDao.getMarketNews() } returns freshCache

        // WHEN
        val result = repository.getMarketNews("AAPL")

        // THEN: No network call is made
        coVerify(exactly = 0) { marketNewsApi.getMarketNews(tickers = any()) }
        assertTrue(result.isSuccess)
        assertEquals(fakeEntity.toDomainModel().title, result.getOrNull()?.first()?.title)
    }

    @Test
    fun `getMarketNews calls network when cache is stale`() = runTest {
        // GIVEN: Cache is 20 minutes old (expired)
        val staleCache = listOf(fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (20 * 60 * 1000L)
        ))
        coEvery { marketNewsDao.getMarketNews() } returns staleCache
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse
        coEvery { marketNewsDao.insert(any()) } returns Unit

        // WHEN
        val result = repository.getMarketNews("AAPL")

        // THEN: Network call is made
        coVerify(exactly = 1) { marketNewsApi.getMarketNews(tickers = "AAPL") }
        assertTrue(result.isSuccess)
        assertEquals(fakeEntity.toDomainModel().title, result.getOrNull()?.first()?.title)
    }

    @Test
    fun `getMarketNews returns stale cache as fallback when network fails`() = runTest {
        // GIVEN: Cache is 20 minutes old (expired)
        val staleCache = listOf(fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (20 * 60 * 1000L)
        ))
        coEvery { marketNewsDao.getMarketNews() } returns staleCache
        coEvery { marketNewsApi.getMarketNews(tickers = any()) } throws IOException("No internet")

        // WHEN
        val result = repository.getMarketNews("AAPL")

        // THEN: Result is success with stale data
        assertTrue(result.isSuccess)
        assertEquals(fakeEntity.toDomainModel().title, result.getOrNull()?.first()?.title)
    }

    @Test(expected = CancellationException::class)
    fun `getMarketNews rethrows CancellationException`() = runTest {
        coEvery { marketNewsDao.getMarketNews() } returns emptyList()
        coEvery { marketNewsApi.getMarketNews(tickers = any()) } throws CancellationException()

        repository.getMarketNews("AAPL")
    }

    @Test
    fun `getMarketNews updates cache on network success`() = runTest {
        coEvery { marketNewsDao.getMarketNews() } returns emptyList()
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse
        coEvery { marketNewsDao.insert(any()) } returns Unit

        repository.getMarketNews("AAPL")

        coVerify(exactly = 1) {
            marketNewsDao.insert(match { it.url == fakeEntity.url })
        }
    }

    @Test
    fun `getMarketNews formats time correctly from API`() = runTest {
        coEvery { marketNewsDao.getMarketNews() } returns emptyList()
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse
        coEvery { marketNewsDao.insert(any()) } returns Unit

        val result = repository.getMarketNews("AAPL")
        val article = result.getOrNull()?.first()

        assertEquals("Apr 10, 2026 13:01", article?.timePublished)
    }

    @Test
    fun `getMarketNews returns failure when cache is empty and network fails`() = runTest {
        coEvery { marketNewsDao.getMarketNews() } returns emptyList()
        val exception = IOException("No internet")
        coEvery { marketNewsApi.getMarketNews(tickers = any()) } throws exception

        val result = repository.getMarketNews("AAPL")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
