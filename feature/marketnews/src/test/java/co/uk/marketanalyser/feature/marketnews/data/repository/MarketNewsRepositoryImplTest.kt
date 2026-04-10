package co.uk.marketanalyser.feature.marketnews.data.repository

import co.uk.marketanalyser.core.network.api.MarketNewsApi
import co.uk.marketanalyser.core.network.dto.MarketNewsItemDto
import co.uk.marketanalyser.core.network.dto.MarketNewsResponse
import co.uk.marketanalyser.core.network.dto.TickerSentimentDto
import co.uk.marketanalyser.core.network.dto.TopicDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class MarketNewsRepositoryImplTest {

    private val marketNewsApi: MarketNewsApi = mockk()
    private val repository = MarketNewsRepositoryImpl(marketNewsApi)

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

    private val fakeResponse = MarketNewsResponse(
        items = "1",
        sentimentScoreDefinition = "",
        relevanceScoreDefinition = "",
        feed = listOf(fakeItem)
    )

    @Test
    fun `getMarketNews returns success with mapped articles on API success`() = runTest {
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse

        val result = repository.getMarketNews("AAPL")

        assertTrue(result.isSuccess)
        val articles = result.getOrNull()!!
        assertEquals(1, articles.size)
        val article = articles.first()
        assertEquals(fakeItem.title, article.title)
        assertEquals(fakeItem.url, article.url)
        assertEquals(fakeItem.summary, article.summary)
        assertEquals("Seeking Alpha", article.source)
        assertEquals("Bullish", article.overallSentimentLabel)
        assertEquals(0.441002, article.overallSentimentScore, 0.000001)
        assertEquals(listOf("earnings", "technology"), article.topics)
        assertEquals(listOf("AAPL"), article.tickers)
    }

    @Test
    fun `getMarketNews formats time correctly`() = runTest {
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse

        val article = repository.getMarketNews("AAPL").getOrNull()!!.first()

        assertEquals("Apr 10, 2026 13:01", article.timePublished)
    }

    @Test
    fun `getMarketNews passes ticker to API`() = runTest {
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse

        repository.getMarketNews("AAPL")

        coVerify(exactly = 1) { marketNewsApi.getMarketNews(tickers = "AAPL") }
    }

    @Test
    fun `getMarketNews returns failure when API throws IOException`() = runTest {
        val exception = IOException("No internet")
        coEvery { marketNewsApi.getMarketNews(tickers = any()) } throws exception

        val result = repository.getMarketNews("AAPL")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getMarketNews returns empty list when feed is empty`() = runTest {
        val emptyResponse = fakeResponse.copy(feed = emptyList())
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns emptyResponse

        val result = repository.getMarketNews("AAPL")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())
    }

    @Test
    fun `getMarketNews handles null banner image gracefully`() = runTest {
        val itemWithNullBanner = fakeItem.copy(bannerImage = null)
        coEvery { marketNewsApi.getMarketNews(tickers = "AAPL") } returns fakeResponse.copy(
            feed = listOf(
                itemWithNullBanner
            )
        )

        val result = repository.getMarketNews("AAPL")

        assertTrue(result.isSuccess)
    }
}

