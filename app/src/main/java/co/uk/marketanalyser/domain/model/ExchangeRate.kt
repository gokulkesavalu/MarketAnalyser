package co.uk.marketanalyser.domain.model

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