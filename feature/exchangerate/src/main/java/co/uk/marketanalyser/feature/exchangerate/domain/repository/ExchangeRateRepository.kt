package co.uk.marketanalyser.feature.exchangerate.domain.repository

import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate

/**
 * Repository interface for managing currency exchange rate data.
 */
interface ExchangeRateRepository {
    /**
     * Fetches the real-time currency exchange rate between two currencies.
     *
     * @param fromCurrency The base currency code (e.g., "USD").
     * @param toCurrency The target currency code (e.g., "GBP").
     * @return A [Result] containing the [ExchangeRate] on success, or a failure exception.
     */
    suspend fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String
    ): Result<ExchangeRate>
}

