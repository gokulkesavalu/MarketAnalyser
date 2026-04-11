package co.uk.marketanalyser.feature.exchangerate.data.repository

import co.uk.marketanalyser.core.database.dao.ExchangeRateDao
import co.uk.marketanalyser.core.network.api.ExchangeRateApi
import co.uk.marketanalyser.feature.exchangerate.data.mapper.toDomainModel
import co.uk.marketanalyser.feature.exchangerate.data.mapper.toEntity
import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate
import co.uk.marketanalyser.feature.exchangerate.domain.repository.ExchangeRateRepository
import javax.inject.Inject

/**
 * Implementation of [ExchangeRateRepository] that provides exchange rate data using an
 * offline-first strategy with a 5-minute cache TTL (Time-To-Live).
 *
 * It attempts to return fresh data from the local cache if available and not expired.
 * Otherwise, it fetches new data from the network and updates the cache.
 *
 * @property exchangeRateDao The local data source for cached exchange rates.
 * @property exchangeRateApi The remote data source for fetching real-time exchange rates.
 */
class ExchangeRateRepositoryImpl @Inject constructor(
    private val exchangeRateDao: ExchangeRateDao,
    private val exchangeRateApi: ExchangeRateApi
) : ExchangeRateRepository {

    internal companion object {
        /** Cache timeout duration in milliseconds (5 minutes). */
        const val CACHE_TIMEOUT = 5 * 60 * 1000L
    }

    /**
     * Fetches the exchange rate for a given currency pair.
     *
     * This method follows an offline-first strategy:
     * 1. Check local database for cached data.
     * 2. If data exists and is fresh (< 5 minutes), return it immediately to conserve API calls.
     * 3. If data is stale or missing, attempt to fetch from the API.
     * 4. On network success, update the local cache and return the result.
     * 5. On network failure, return the stale cache as a fallback if it exists.
     *
     * @param fromCurrency The base currency code (e.g., "USD").
     * @param toCurrency The target currency code (e.g., "GBP").
     * @return A [Result] containing the [ExchangeRate] domain model or an error.
     */
    override suspend fun getExchangeRate(
        fromCurrency: String,
        toCurrency: String
    ): Result<ExchangeRate> {
        val cachedExchangeRate = exchangeRateDao.getExchangeRate("${fromCurrency}_$toCurrency")
        val isCacheValid = cachedExchangeRate?.let {
            System.currentTimeMillis() - it.cachedAt < CACHE_TIMEOUT
        }
        if (isCacheValid == true) {
            return Result.success(cachedExchangeRate.toDomainModel())
        }
        return try {
            val dto = exchangeRateApi.getCurrencyExchangeRate(
                fromCurrency = fromCurrency,
                toCurrency = toCurrency
            ).realtimeCurrencyExchangeRate
            exchangeRateDao.insert(dto.toEntity(fromCurrency, toCurrency))
            Result.success(dto.toDomainModel())
        } catch (e: Exception) {
            cachedExchangeRate?.let {
                Result.success(it.toDomainModel())
            } ?: Result.failure(e)
        }
    }
}

