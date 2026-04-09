package co.uk.marketanalyser.data.repository

import co.uk.marketanalyser.data.api.MarketApi
import co.uk.marketanalyser.domain.repository.ExchangeRateRepository
import co.uk.marketanalyser.domain.model.ExchangeRate
import javax.inject.Inject

class ExchangeRateRepositoryImpl @Inject constructor(
    private val api: MarketApi
) : ExchangeRateRepository {

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