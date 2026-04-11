package co.uk.marketanalyser.core.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Root response object for the Alpha Vantage currency exchange rate API.
 */
data class ExchangeRateResponse(
    @SerializedName("Realtime Currency Exchange Rate")
    val realtimeCurrencyExchangeRate: ExchangeRateDto
)

/**
 * Data Transfer Object (DTO) for the real-time currency exchange rate.
 */
data class ExchangeRateDto(
    @SerializedName("1. From_Currency Code")
    val fromCurrencyCode: String,

    @SerializedName("2. From_Currency Name")
    val fromCurrencyName: String,

    @SerializedName("3. To_Currency Code")
    val toCurrencyCode: String,

    @SerializedName("4. To_Currency Name")
    val toCurrencyName: String,

    @SerializedName("5. Exchange Rate")
    val exchangeRate: String,

    @SerializedName("6. Last Refreshed")
    val lastRefreshed: String,

    @SerializedName("7. Time Zone")
    val timeZone: String,

    @SerializedName("8. Bid Price")
    val bidPrice: String,

    @SerializedName("9. Ask Price")
    val askPrice: String
)

