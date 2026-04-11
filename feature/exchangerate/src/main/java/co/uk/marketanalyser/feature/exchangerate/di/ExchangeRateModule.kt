package co.uk.marketanalyser.feature.exchangerate.di

import android.content.Context
import androidx.room.Room
import co.uk.marketanalyser.feature.exchangerate.data.local.ExchangeRateDatabase
import co.uk.marketanalyser.feature.exchangerate.data.repository.ExchangeRateRepositoryImpl
import co.uk.marketanalyser.feature.exchangerate.domain.repository.ExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the [ExchangeRateRepository] interface to its implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class ExchangeRateModule {

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(
        impl: ExchangeRateRepositoryImpl
    ): ExchangeRateRepository

    companion object {

        @Provides
        @Singleton
        fun provideExchangeRateDatabase(@ApplicationContext app: Context): ExchangeRateDatabase =
            Room.databaseBuilder(
                app,
                ExchangeRateDatabase::class.java,
                "exchange_rate_db"
            ).build()

        @Provides
        @Singleton
        fun provideExchangeRateDao(db: ExchangeRateDatabase) = db.exchangeRateDao()
    }
}
