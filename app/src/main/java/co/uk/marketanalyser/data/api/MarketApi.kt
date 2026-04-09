package co.uk.marketanalyser.data.api

import co.uk.marketanalyser.data.model.ExchangeRateResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketApi {

    @GET("query")
    suspend fun getCurrencyExchangeRate(
        @Query("function") function: String = "CURRENCY_EXCHANGE_RATE",
        @Query("from_currency") fromCurrency: String,
        @Query("to_currency") toCurrency: String
    ): ExchangeRateResponse
}