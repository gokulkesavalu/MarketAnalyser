package co.uk.marketanalyser.feature.exchangerate.domain.model

/**
 * Domain model representing a currency exchange rate.
 */
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

