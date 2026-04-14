package co.uk.marketanalyser.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import co.uk.marketanalyser.core.database.entity.ExchangeRateEntity

/**
 * Data Access Object for the exchange_rates table.
 *
 * Provides methods for persisting and retrieving currency exchange rates.
 */
@Dao
interface ExchangeRateDao {
    /**
     * Inserts or updates an exchange rate record.
     *
     * @param exchangeRate The exchange rate entity to save.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRateEntity)

    /**
     * Retrieves an exchange rate record by its unique ID.
     *
     * @param id The unique identifier for the currency pair (e.g., "USD_JPY").
     * @return The cached [ExchangeRateEntity] or null if not found.
     */
    @Query("SELECT * FROM exchange_rates WHERE id = :id")
    suspend fun getExchangeRate(id: String): ExchangeRateEntity?
}