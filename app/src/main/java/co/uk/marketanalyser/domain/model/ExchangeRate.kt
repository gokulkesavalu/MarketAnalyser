package co.uk.marketanalyser.domain.model

import androidx.compose.runtime.Immutable

/**
 * Domain model representing a currency exchange rate.
 *
 * @property fromCurrencyCode The base currency code (e.g., "USD").
 * @property fromCurrencyName The base currency name (e.g., "United States Dollar").
 * @property toCurrencyCode The target currency code (e.g., "GBP").
 * @property toCurrencyName The target currency name (e.g., "British Pound Sterling").
 * @property exchangeRate The real-time exchange rate value.
 * @property lastRefreshed The date and time the data was last updated.
 * @property timeZone The time zone of the last refresh time.
 * @property bidPrice The current bid price for the currency pair.
 * @property askPrice The current ask price for the currency pair.
 */
@Immutable
data class ExchangeRate(
    val fromCurrencyCode: String,
    val fromCurrencyName: String,
    val toCurrencyCode: String,
    val toCurrencyName: String,
    val exchangeRate: Double,
    val lastRefreshed: String,
    val timeZone: String,
    val bidPrice: Double,
    val askPrice: Double
)