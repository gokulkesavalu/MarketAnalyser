package co.uk.marketanalyser.data.api

import co.uk.marketanalyser.data.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for Alpha Vantage currency exchange rate services.
 */
interface ExchangeRateApi {

    /**
     * Fetches the real-time currency exchange rate.
     *
     * @param function The API function (default: "CURRENCY_EXCHANGE_RATE").
     * @param fromCurrency The base currency code.
     * @param toCurrency The target currency code.
     * @return [ExchangeRateResponse] containing the exchange rate data.
     */
    @GET("query")
    suspend fun getCurrencyExchangeRate(
        @Query("function") function: String = "CURRENCY_EXCHANGE_RATE",
        @Query("from_currency") fromCurrency: String,
        @Query("to_currency") toCurrency: String
    ): ExchangeRateResponse
}