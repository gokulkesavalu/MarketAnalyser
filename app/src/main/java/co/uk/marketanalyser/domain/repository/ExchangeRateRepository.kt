package co.uk.marketanalyser.domain.repository

import co.uk.marketanalyser.domain.model.ExchangeRate

interface ExchangeRateRepository {
    suspend fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String
    ): Result<ExchangeRate>
}