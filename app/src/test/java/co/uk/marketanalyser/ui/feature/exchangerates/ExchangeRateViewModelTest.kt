package co.uk.marketanalyser.ui.feature.exchangerates

import co.uk.marketanalyser.domain.model.ExchangeRate
import co.uk.marketanalyser.domain.repository.ExchangeRateRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ExchangeRateViewModelTest {

    private val repository: ExchangeRateRepository = mockk()
    private lateinit var viewModel: ExchangeRateViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val fakeExchangeRate = ExchangeRate(
        fromCurrencyCode = "USD",
        fromCurrencyName = "United States Dollar",
        toCurrencyCode = "JPY",
        toCurrencyName = "Japanese Yen",
        exchangeRate = 158.75,
        lastRefreshed = "2026-04-09 17:49:38",
        timeZone = "UTC",
        bidPrice = 158.74,
        askPrice = 158.76
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = ExchangeRateViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() {
        val state = viewModel.uiState.value
        assertEquals("", state.fromCurrency)
        assertEquals("", state.toCurrency)
        assertNull(state.exchangeRate)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `onFromCurrencyChange updates fromCurrency and clears error`() {
        viewModel.onFromCurrencyChange("usd")

        val state = viewModel.uiState.value
        assertEquals("USD", state.fromCurrency)
        assertNull(state.error)
    }

    @Test
    fun `onToCurrencyChange updates toCurrency and clears error`() {
        viewModel.onToCurrencyChange("jpy")

        val state = viewModel.uiState.value
        assertEquals("JPY", state.toCurrency)
        assertNull(state.error)
    }

    @Test
    fun `fetchExchangeRate sets error when fromCurrency is blank`() {
        viewModel.onToCurrencyChange("JPY")
        viewModel.fetchExchangeRate()

        val state = viewModel.uiState.value
        assertEquals("Please enter both currencies", state.error)
    }

    @Test
    fun `fetchExchangeRate sets error when toCurrency is blank`() {
        viewModel.onFromCurrencyChange("USD")
        viewModel.fetchExchangeRate()

        val state = viewModel.uiState.value
        assertEquals("Please enter both currencies", state.error)
    }

    @Test
    fun `fetchExchangeRate updates state to success`() = runTest {
        viewModel.onFromCurrencyChange("USD")
        viewModel.onToCurrencyChange("JPY")

        coEvery {
            repository.getExchangeRate("USD", "JPY")
        } returns Result.success(fakeExchangeRate)

        viewModel.fetchExchangeRate()
        advanceUntilIdle()

        // Check success state
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals(fakeExchangeRate, state.exchangeRate)
        assertNull(state.error)
    }

    @Test
    fun `fetchExchangeRate updates state to error on failure`() = runTest {
        viewModel.onFromCurrencyChange("USD")
        viewModel.onToCurrencyChange("JPY")

        val errorMessage = "Network error"
        coEvery {
            repository.getExchangeRate("USD", "JPY")
        } returns Result.failure(Exception(errorMessage))

        viewModel.fetchExchangeRate()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.exchangeRate)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `fetchExchangeRate uses unknown error when exception message is null`() = runTest {
        viewModel.onFromCurrencyChange("USD")
        viewModel.onToCurrencyChange("JPY")

        coEvery {
            repository.getExchangeRate("USD", "JPY")
        } returns Result.failure(Exception())

        viewModel.fetchExchangeRate()
        advanceUntilIdle()

        assertEquals("Unknown error", viewModel.uiState.value.error)
    }
}
