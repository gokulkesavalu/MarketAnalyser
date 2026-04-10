package co.uk.marketanalyser.feature.marketnews.di

import co.uk.marketanalyser.feature.marketnews.data.repository.MarketNewsRepositoryImpl
import co.uk.marketanalyser.feature.marketnews.domain.repository.MarketNewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module that binds the [MarketNewsRepository] interface to its implementation.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class MarketNewsModule {

    @Binds
    @Singleton
    abstract fun bindMarketNewsRepository(
        impl: MarketNewsRepositoryImpl
    ): MarketNewsRepository
}

