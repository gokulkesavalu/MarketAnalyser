package co.uk.marketanalyser.di

import co.uk.marketanalyser.BuildConfig
import co.uk.marketanalyser.data.repository.ExchangeRateRepositoryImpl
import co.uk.marketanalyser.data.api.MarketApi
import co.uk.marketanalyser.domain.repository.ExchangeRateRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(
        impl: ExchangeRateRepositoryImpl
    ): ExchangeRateRepository

    companion object {

        @Provides
        @Singleton
        fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
            val request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("apikey", BuildConfig.ALPHA_VANTAGE_API_KEY)
                .build()
            chain.proceed(request.newBuilder().url(url).build())
        }

        @Provides
        @Singleton
        fun provideLoggingInterceptor(): HttpLoggingInterceptor =
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG)
                    HttpLoggingInterceptor.Level.BODY
                else
                    HttpLoggingInterceptor.Level.NONE
            }

        @Provides
        @Singleton
        fun provideOkHttpClient(
            apiKeyInterceptor: Interceptor,
            loggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        @Provides
        @Singleton
        fun provideMarketApi(retrofit: Retrofit): MarketApi =
            retrofit.create(MarketApi::class.java)
    }
}
