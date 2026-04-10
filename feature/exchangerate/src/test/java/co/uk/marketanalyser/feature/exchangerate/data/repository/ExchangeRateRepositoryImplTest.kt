package co.uk.marketanalyser.feature.exchangerate.data.repository

import co.uk.marketanalyser.core.network.api.ExchangeRateApi
import co.uk.marketanalyser.core.network.dto.ExchangeRateDto
import co.uk.marketanalyser.core.network.dto.ExchangeRateResponse
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
    private val repository = ExchangeRateRepositoryImpl(exchangeRateApi)

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
    fun `getExchangeRate returns success with mapped ExchangeRate on API success`() = runTest {
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        } returns fakeResponse

        val result = repository.getExchangeRate("USD", "JPY")

        assertTrue(result.isSuccess)
        assertEquals(expectedRate, result.getOrNull())
    }

    @Test
    fun `getExchangeRate passes correct currency codes to API`() = runTest {
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "GBP",
                toCurrency = "EUR"
            )
        } returns fakeResponse

        repository.getExchangeRate("GBP", "EUR")

        coVerify(exactly = 1) {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "GBP",
                toCurrency = "EUR"
            )
        }
    }

    @Test
    fun `getExchangeRate returns failure when API throws IOException`() = runTest {
        val exception = IOException("No internet connection")
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = any(),
                toCurrency = any()
            )
        } throws exception

        val result = repository.getExchangeRate("USD", "JPY")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getExchangeRate returns failure when API throws RuntimeException`() = runTest {
        val exception = RuntimeException("Server error")
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = any(),
                toCurrency = any()
            )
        } throws exception

        val result = repository.getExchangeRate("USD", "JPY")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getExchangeRate maps string exchange rate to Double correctly`() = runTest {
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

        val rate = repository.getExchangeRate("USD", "GBP").getOrNull()!!

        assertEquals(1.23456789, rate.exchangeRate, 0.000000001)
        assertEquals(1.23456000, rate.bidPrice, 0.000000001)
        assertEquals(1.23457000, rate.askPrice, 0.000000001)
    }

    @Test
    fun `getExchangeRate maps all DTO fields correctly`() = runTest {
        coEvery {
            exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = "USD",
                toCurrency = "JPY"
            )
        } returns fakeResponse

        val rate = repository.getExchangeRate("USD", "JPY").getOrNull()!!

        assertEquals("USD", rate.fromCurrencyCode)
        assertEquals("United States Dollar", rate.fromCurrencyName)
        assertEquals("JPY", rate.toCurrencyCode)
        assertEquals("Japanese Yen", rate.toCurrencyName)
        assertEquals("2026-04-09 17:49:38", rate.lastRefreshed)
        assertEquals("UTC", rate.timeZone)
    }
}

