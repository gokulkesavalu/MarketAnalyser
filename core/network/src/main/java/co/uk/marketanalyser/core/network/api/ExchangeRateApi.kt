package co.uk.marketanalyser.core.network.api

import co.uk.marketanalyser.core.network.dto.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeRateApi {
    /**
     * Fetches the real-time currency exchange rate.
     *
     * @param function The API function (default: "CURRENCY_EXCHANGE_RATE").
     * @param fromCurrency The base currency code (e.g., "USD").
     * @param toCurrency The target currency code (e.g., "GBP").
     * @return [ExchangeRateResponse] containing the exchange rate data.
     */
    @GET("query")
    suspend fun getCurrencyExchangeRate(
        @Query("function") function: String = "CURRENCY_EXCHANGE_RATE",
        @Query("from_currency") fromCurrency: String,
        @Query("to_currency") toCurrency: String
    ): ExchangeRateResponse
}