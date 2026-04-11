package co.uk.marketanalyser.core.database.di

import android.content.Context
import androidx.room.Room
import co.uk.marketanalyser.core.database.MarketAnalyserDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing database-related dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    /**
     * Provides a singleton instance of [MarketAnalyserDatabase].
     *
     * @param app The application context.
     * @return The initialized Room database.
     */
    @Provides
    @Singleton
    fun provideExchangeRateDatabase(@ApplicationContext app: Context): MarketAnalyserDatabase =
        Room.databaseBuilder(
            app,
            MarketAnalyserDatabase::class.java,
            "exchange_rate_db"
        ).build()

    /**
     * Provides the DAO for exchange rate operations.
     *
     * @param db The [MarketAnalyserDatabase] instance.
     * @return The [ExchangeRateDao] used for database operations.
     */
    @Provides
    @Singleton
    fun provideExchangeRateDao(db: MarketAnalyserDatabase) = db.exchangeRateDao()
}