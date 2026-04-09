package co.uk.marketanalyser.data.model

import com.google.gson.annotations.SerializedName

/**
 * Root response object for the Alpha Vantage currency exchange rate API.
 *
 * @property realtimeCurrencyExchangeRate The nested exchange rate data object.
 */
data class ExchangeRateResponse(
    @SerializedName("Realtime Currency Exchange Rate")
    val realtimeCurrencyExchangeRate: ExchangeRateDto
)

/**
 * Data Transfer Object (DTO) for the real-time currency exchange rate.
 * Maps directly to the Alpha Vantage API response format.
 *
 * @property fromCurrencyCode The code for the base currency (e.g., "USD").
 * @property fromCurrencyName The full name of the base currency.
 * @property toCurrencyCode The code for the target currency (e.g., "EUR").
 * @property toCurrencyName The full name of the target currency.
 * @property exchangeRate The real-time exchange rate as a string.
 * @property lastRefreshed The date and time the rate was last updated.
 * @property timeZone The time zone for the refresh time.
 * @property bidPrice The current bid price as a string.
 * @property askPrice The current ask price as a string.
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

