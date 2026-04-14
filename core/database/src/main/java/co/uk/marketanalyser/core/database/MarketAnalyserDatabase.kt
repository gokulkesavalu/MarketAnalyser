package co.uk.marketanalyser.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import co.uk.marketanalyser.core.database.dao.ExchangeRateDao
import co.uk.marketanalyser.core.database.dao.MarketNewsDao
import co.uk.marketanalyser.core.database.entity.ExchangeRateEntity
import co.uk.marketanalyser.core.database.entity.MarketNewsEntity
import co.uk.marketanalyser.core.database.typeconverters.MarketNewsTypeConverters

/**
 * The main Room database for the Market Analyser application.
 *
 * This database serves as the local persistent store for market data, enabling an
 * offline-first experience. It caches data such as real-time currency exchange rates
 * and market news sentiment.
 *
 * The database uses [MarketNewsTypeConverters] to handle the conversion of complex
 * data types (e.g., lists of objects) into a format suitable for SQLite storage.
 */
@Database(
    entities = [
        ExchangeRateEntity::class,
        MarketNewsEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(MarketNewsTypeConverters::class)
abstract class MarketAnalyserDatabase : RoomDatabase() {
    /**
     * Provides access to the [ExchangeRateDao] for performing database operations
     * on exchange rate data.
     *
     * @return An instance of [ExchangeRateDao].
     */
    abstract fun exchangeRateDao(): ExchangeRateDao

    /**
     * Provides access to the [MarketNewsDao] for performing database operations
     * on market news data.
     *
     * @return An instance of [MarketNewsDao].
     */
    abstract fun marketNewsDao(): MarketNewsDao
}