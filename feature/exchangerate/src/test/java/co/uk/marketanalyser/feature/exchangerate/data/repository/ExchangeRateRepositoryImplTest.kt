package co.uk.marketanalyser.feature.exchangerate.data.repository

import co.uk.marketanalyser.core.database.dao.ExchangeRateDao
import co.uk.marketanalyser.core.network.api.ExchangeRateApi
import co.uk.marketanalyser.core.network.dto.ExchangeRateDto
import co.uk.marketanalyser.core.network.dto.ExchangeRateResponse
import co.uk.marketanalyser.feature.exchangerate.data.mapper.toEntity
import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ExchangeRateRepositoryImplTest {

    private val exchangeRateApi: ExchangeRateApi = mockk()
    private val exchangeRateDao: ExchangeRateDao = mockk()
    private val repository = ExchangeRateRepositoryImpl(exchangeRateDao, exchangeRateApi)

    private val fakeDto = ExchangeRateDto(
        fromCurrencyCode = "USD",
        fromCurrencyName = "United States Dollar",
        toCurrencyCode = "JPY",
        toCurrencyName = "Japanese Yen",
        exchangeRate = "158.74988722",
        lastRefreshed = "2026-04-09 17:49:38",
        timeZone = "UTC",
        bidPrice = "158.74831560",
        askPrice = "158.75695953"
    )

    private val fakeEntity = fakeDto.toEntity("USD", "JPY")

    private val fakeResponse = ExchangeRateResponse(realtimeCurrencyExchangeRate = fakeDto)

    private val expectedRate = ExchangeRate(
        fromCurrencyCode = "USD",
        fromCurrencyName = "United States Dollar",
        toCurrencyCode = "JPY",
        toCurrencyName = "Japanese Yen",
        exchangeRate = 158.74988722,
        lastRefreshed = "2026-04-09 17:49:38",
        timeZone = "UTC",
        bidPrice = 158.74831560,
        askPrice = 158.75695953
    )

    @Test
    fun `getExchangeRate returns cached data when fresh`() = runTest {
        // GIVEN: Cache is only 1 minute old
        val freshCache = fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (1 * 60 * 1000L)
        )
        coEvery { exchangeRateDao.getExchangeRate(any()) } returns freshCache

        // WHEN
        val result = repository.getExchangeRate("USD", "JPY")

        // THEN: No network call is made
        coVerify(exactly = 0) {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        }
        assertTrue(result.isSuccess)
        assertEquals(expectedRate, result.getOrNull())
    }

    @Test
    fun `getExchangeRate calls network when cache is stale`() = runTest {
        // GIVEN: Cache is 6 minutes old (expired)
        val staleCache = fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (6 * 60 * 1000L)
        )
        coEvery { exchangeRateDao.getExchangeRate(any()) } returns staleCache
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        } returns fakeResponse
        coEvery { exchangeRateDao.insert(any()) } returns Unit

        val result = repository.getExchangeRate("USD", "JPY")

        coVerify(exactly = 1) {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        }
        assertTrue(result.isSuccess)
        assertEquals(expectedRate, result.getOrNull())
    }

    @Test
    fun `getExchangeRate returns stale cache as fallback when network fails`() = runTest {
        // GIVEN: Cache is 6 minutes old (expired)
        val staleCache = fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (6 * 60 * 1000L)
        )
        coEvery { exchangeRateDao.getExchangeRate(any()) } returns staleCache
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        } throws IOException("No internet connection")

        val result = repository.getExchangeRate("USD", "JPY")

        coVerify(exactly = 1) {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        }
        assertTrue(result.isSuccess)
        assertEquals(expectedRate, result.getOrNull())
    }

    @Test
    fun `getExchangeRate passes correct currency codes to API`() = runTest {
        val staleCache = fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (6 * 60 * 1000L)
        )
        coEvery { exchangeRateDao.getExchangeRate(any()) } returns staleCache
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "GBP",
                toCurrency = "EUR"
            )
        } returns fakeResponse
        coEvery { exchangeRateDao.insert(any()) } returns Unit

        repository.getExchangeRate("GBP", "EUR")

        coVerify(exactly = 1) {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "GBP",
                toCurrency = "EUR"
            )
        }
    }

    @Test
    fun `getExchangeRate maps string exchange rate to Double correctly`() = runTest {
        val staleCache = fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (6 * 60 * 1000L)
        )
        coEvery { exchangeRateDao.getExchangeRate(any()) } returns staleCache
        val customDto = fakeDto.copy(
            exchangeRate = "1.23456789",
            bidPrice = "1.23456000",
            askPrice = "1.23457000"
        )
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = any(),
                toCurrency = any()
            )
        } returns ExchangeRateResponse(customDto)
        coEvery { exchangeRateDao.insert(any()) } returns Unit

        val rate = repository.getExchangeRate("USD", "GBP").getOrNull()!!

        assertEquals(1.23456789, rate.exchangeRate, 0.000000001)
        assertEquals(1.23456000, rate.bidPrice, 0.000000001)
        assertEquals(1.23457000, rate.askPrice, 0.000000001)
    }

    @Test
    fun `getExchangeRate maps all DTO fields correctly`() = runTest {
        val staleCache = fakeEntity.copy(
            cachedAt = System.currentTimeMillis() - (6 * 60 * 1000L)
        )
        coEvery { exchangeRateDao.getExchangeRate(any()) } returns staleCache
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        } returns fakeResponse
        coEvery { exchangeRateDao.insert(any()) } returns Unit

        val rate = repository.getExchangeRate("USD", "JPY").getOrNull()!!

        coVerify(exactly = 1) {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        }

        assertEquals("USD", rate.fromCurrencyCode)
        assertEquals("United States Dollar", rate.fromCurrencyName)
        assertEquals("JPY", rate.toCurrencyCode)
        assertEquals("Japanese Yen", rate.toCurrencyName)
        assertEquals("2026-04-09 17:49:38", rate.lastRefreshed)
        assertEquals("UTC", rate.timeZone)
    }
}

