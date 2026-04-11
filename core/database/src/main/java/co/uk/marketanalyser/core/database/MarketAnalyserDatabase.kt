package co.uk.marketanalyser.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.uk.marketanalyser.core.database.dao.ExchangeRateDao
import co.uk.marketanalyser.core.database.entity.ExchangeRateEntity

/**
 * The main Room database for the Market Analyser application.
 * This database stores cached market data, such as exchange rates.
 */
@Database(entities = [ExchangeRateEntity::class], version = 1, exportSchema = false)
abstract class MarketAnalyserDatabase : RoomDatabase() {
    /**
     * Provides access to the [ExchangeRateDao] for performing database operations
     * on exchange rate data.
     *
     * @return An instance of [ExchangeRateDao].
     */
    abstract fun exchangeRateDao(): ExchangeRateDao
}