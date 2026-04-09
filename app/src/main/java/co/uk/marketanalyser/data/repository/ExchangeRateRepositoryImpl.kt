package co.uk.marketanalyser.data.repository

import co.uk.marketanalyser.data.api.ExchangeRateApi
import co.uk.marketanalyser.domain.repository.ExchangeRateRepository
import co.uk.marketanalyser.domain.model.ExchangeRate
import javax.inject.Inject

/**
 * Implementation of [ExchangeRateRepository] that fetches data from [ExchangeRateApi].
 */
class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: ExchangeRateApi
) : ExchangeRateRepository {

    /**
     * Fetches the exchange rate and maps it to the domain model [ExchangeRate].
     *
     * @param fromCurrency The source currency code (e.g., "USD").
     * @param toCurrency The destination currency code (e.g., "GBP").
     * @return A [Result] containing the [ExchangeRate] if successful, or a failure otherwise.
     */
    override suspend fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String
    ): Result<ExchangeRate> {
        return try {
            val response = api.getCurrencyExchangeRate(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency
            )
            val dto = response.realtimeCurrencyExchangeRate
            Result.success(
                ExchangeRate(
                    fromCurrencyCode = dto.fromCurrencyCode,
                    fromCurrencyName = dto.fromCurrencyName,
                    toCurrencyCode = dto.toCurrencyCode,
                    toCurrencyName = dto.toCurrencyName,
                    exchangeRate = dto.exchangeRate.toDouble(),
                    lastRefreshed = dto.lastRefreshed,
                    timeZone = dto.timeZone,
                    bidPrice = dto.bidPrice.toDouble(),
                    askPrice = dto.askPrice.toDouble()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}