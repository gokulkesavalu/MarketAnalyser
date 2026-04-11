package co.uk.marketanalyser.feature.exchangerate.data.mapper

import co.uk.marketanalyser.core.database.entity.ExchangeRateEntity
import co.uk.marketanalyser.core.network.dto.ExchangeRateDto
import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate

/**
 * Maps an [ExchangeRateEntity] to a domain-level [ExchangeRate] model.
 *
 * @return The domain representation of the cached exchange rate.
 */
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

/**
 * Maps an [ExchangeRateDto] to a domain-level [ExchangeRate] model.
 *
 * @return The domain representation of the exchange rate.
 */
fun ExchangeRateDto.toDomainModel() = ExchangeRate(
    fromCurrencyCode = fromCurrencyCode,
    fromCurrencyName = fromCurrencyName,
    toCurrencyCode = toCurrencyCode,
    toCurrencyName = toCurrencyName,
    exchangeRate = exchangeRate.toDouble(),
    lastRefreshed = lastRefreshed,
    timeZone = timeZone,
    bidPrice = bidPrice.toDouble(),
    askPrice = askPrice.toDouble()
)

/**
 * Maps an [ExchangeRateDto] to its persistent [ExchangeRateEntity] representation.
 *
 * @param fromCurrency The base currency code (e.g., "USD").
 * @param toCurrency The target currency code (e.g., "GBP").
 * @return The persistent entity to be stored in the database.
 */
fun ExchangeRateDto.toEntity(fromCurrency: String, toCurrency: String) = ExchangeRateEntity(
    id = "${fromCurrency}_${toCurrency}",
    fromCurrencyCode = fromCurrencyCode,
    fromCurrencyName = fromCurrencyName,
    toCurrencyCode = toCurrencyCode,
    toCurrencyName = toCurrencyName,
    exchangeRate = exchangeRate.toDouble(),
    lastRefreshed = lastRefreshed,
    timeZone = timeZone,
    bidPrice = bidPrice.toDouble(),
    askPrice = askPrice.toDouble()
)