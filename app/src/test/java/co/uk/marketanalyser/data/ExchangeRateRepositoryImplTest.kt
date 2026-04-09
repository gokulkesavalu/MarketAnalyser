package co.uk.marketanalyser.data

import co.uk.marketanalyser.data.api.MarketApi
import co.uk.marketanalyser.data.model.ExchangeRateDto
import co.uk.marketanalyser.data.model.ExchangeRateResponse
import co.uk.marketanalyser.data.repository.ExchangeRateRepositoryImpl
import co.uk.marketanalyser.domain.model.ExchangeRate
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class ExchangeRateRepositoryImplTest {

    private val api: MarketApi = mockk()
    private val repository: ExchangeRateRepositoryImpl = ExchangeRateRepositoryImpl(api)

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

    private val fakeResponse = ExchangeRateResponse(
        realtimeCurrencyExchangeRate = fakeDto
    )

    private val expectedExchangeRate = ExchangeRate(
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
            api.getCurrencyExchangeRate(fromCurrency = "USD", toCurrency = "JPY")
        } returns fakeResponse

        val result = repository.getExchangeRate("USD", "JPY")

        assertTrue(result.isSuccess)
        assertEquals(expectedExchangeRate, result.getOrNull())
    }

    @Test
    fun `getExchangeRate passes correct currency codes to API`() = runTest {
        coEvery {
            api.getCurrencyExchangeRate(fromCurrency = "GBP", toCurrency = "EUR")
        } returns fakeResponse

        repository.getExchangeRate("GBP", "EUR")

        coVerify(exactly = 1) {
            api.getCurrencyExchangeRate(fromCurrency = "GBP", toCurrency = "EUR")
        }
    }

    @Test
    fun `getExchangeRate returns failure when API throws IOException`() = runTest {
        val exception = IOException("No internet connection")
        coEvery {
            api.getCurrencyExchangeRate(fromCurrency = any(), toCurrency = any())
        } throws exception

        val result = repository.getExchangeRate("USD", "JPY")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getExchangeRate returns failure when API throws RuntimeException`() = runTest {
        val exception = RuntimeException("Server error")
        coEvery {
            api.getCurrencyExchangeRate(fromCurrency = any(), toCurrency = any())
        } throws exception

        val result = repository.getExchangeRate("USD", "JPY")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }

    @Test
    fun `getExchangeRate maps string exchange rate to Double correctly`() = runTest {
        val dtoWithPreciseRate = fakeDto.copy(
            exchangeRate = "1.23456789",
            bidPrice = "1.23456000",
            askPrice = "1.23457000"
        )
        coEvery {
            api.getCurrencyExchangeRate(fromCurrency = any(), toCurrency = any())
        } returns ExchangeRateResponse(dtoWithPreciseRate)

        val result = repository.getExchangeRate("USD", "GBP")

        val rate = result.getOrNull()!!
        assertEquals(1.23456789, rate.exchangeRate, 0.000000001)
        assertEquals(1.23456000, rate.bidPrice, 0.000000001)
        assertEquals(1.23457000, rate.askPrice, 0.000000001)
    }

    @Test
    fun `getExchangeRate maps all DTO fields correctly`() = runTest {
        coEvery {
            api.getCurrencyExchangeRate(fromCurrency = "USD", toCurrency = "JPY")
        } returns fakeResponse

        val result = repository.getExchangeRate("USD", "JPY")
        val rate = result.getOrNull()!!

        assertEquals("USD", rate.fromCurrencyCode)
        assertEquals("United States Dollar", rate.fromCurrencyName)
        assertEquals("JPY", rate.toCurrencyCode)
        assertEquals("Japanese Yen", rate.toCurrencyName)
        assertEquals("2026-04-09 17:49:38", rate.lastRefreshed)
        assertEquals("UTC", rate.timeZone)
    }
}

