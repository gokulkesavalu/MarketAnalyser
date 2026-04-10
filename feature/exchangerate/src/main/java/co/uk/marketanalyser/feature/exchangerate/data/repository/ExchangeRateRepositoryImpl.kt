package co.uk.marketanalyser.feature.exchangerate.data.repository

import co.uk.marketanalyser.core.network.api.ExchangeRateApi
import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate
import co.uk.marketanalyser.feature.exchangerate.domain.repository.ExchangeRateRepository
import javax.inject.Inject

/**
 * Implementation of [ExchangeRateRepository] that fetches data from [ExchangeRateApi].
 */
class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateApi: ExchangeRateApi
) : ExchangeRateRepository {

    /**
     * Fetches the exchange rate and maps the network DTO to the domain model [ExchangeRate].
     */
    override suspend fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String
    ): Result<ExchangeRate> {
        return try {
            val dto = exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency
            ).realtimeCurrencyExchangeRate
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

