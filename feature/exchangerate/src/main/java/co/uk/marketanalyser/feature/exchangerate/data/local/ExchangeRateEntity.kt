package co.uk.marketanalyser.feature.exchangerate.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate

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

fun ExchangeRateEntity.toDomainModel() = ExchangeRate(
    fromCurrencyCode = fromCurrencyCode,
    fromCurrencyName = fromCurrencyName,
    toCurrencyCode = toCurrencyCode,
    toCurrencyName = toCurrencyName,
    exchangeRate = exchangeRate,
    lastRefreshed = lastRefreshed,
    timeZone = timeZone,
    bidPrice = bidPrice,
    askPrice = askPrice
)

fun ExchangeRate.toEntity(fromCurrency: String, toCurrency: String) = ExchangeRateEntity(
    id = "${fromCurrency}_${toCurrency}",
    fromCurrencyCode = fromCurrencyCode,
    fromCurrencyName = fromCurrencyName,
    toCurrencyCode = toCurrencyCode,
    toCurrencyName = toCurrencyName,
    exchangeRate = exchangeRate,
    lastRefreshed = lastRefreshed,
    timeZone = timeZone,
    bidPrice = bidPrice,
    askPrice = askPrice
)