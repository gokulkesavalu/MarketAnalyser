package co.uk.marketanalyser.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room Entity representing a cached currency exchange rate.
 *
 * @property id Unique identifier for the currency pair (e.g., "USD_JPY").
 * @property fromCurrencyCode The base currency code.
 * @property fromCurrencyName The full name of the base currency.
 * @property toCurrencyCode The target currency code.
 * @property toCurrencyName The full name of the target currency.
 * @property exchangeRate The real-time exchange rate value.
 * @property lastRefreshed The date and time the rate was last updated (from API).
 * @property timeZone The time zone for the refresh time.
 * @property bidPrice The current bid price.
 * @property askPrice The current ask price.
 * @property cachedAt System timestamp when this record was saved to the local database.
 */
@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey val id: String, // e.g. "USD_JPY" — one row per currency pair
    val fromCurrencyCode: String,
    val fromCurrencyName: String,
    val toCurrencyCode: String,
    val toCurrencyName: String,
    val exchangeRate: Double,
    val lastRefreshed: String,
    val timeZone: String,
    val bidPrice: Double,
    val askPrice: Double,
    val cachedAt: Long = System.currentTimeMillis()
)