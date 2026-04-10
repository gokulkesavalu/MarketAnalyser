package co.uk.marketanalyser.feature.exchangerate.di

import co.uk.marketanalyser.feature.exchangerate.data.repository.ExchangeRateRepositoryImpl
import co.uk.marketanalyser.feature.exchangerate.domain.repository.ExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
}

