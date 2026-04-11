package co.uk.marketanalyser.di

import co.uk.marketanalyser.BuildConfig
import co.uk.marketanalyser.core.network.api.ExchangeRateApi
import co.uk.marketanalyser.core.network.api.MarketNewsApi
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

/**
 * Hilt module that provides the network layer — Retrofit, OkHttp, and [MarketNewsApi, ExchangeRateApi].
 * Lives in :app so it has access to [BuildConfig] for the API key and base URL.
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    /**
     * Provides an [Interceptor] that automatically appends the API key to every request.
     */
    @Provides
    @Singleton
    fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
        val url = chain.request().url.newBuilder()
            .addQueryParameter("apikey", BuildConfig.ALPHA_VANTAGE_API_KEY)
            .build()
        chain.proceed(chain.request().newBuilder().url(url).build())
    }

    /**
     * Provides a [HttpLoggingInterceptor] — logs full body in debug, nothing in release.
     */
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE
        }

    /**
     * Provides a configured [OkHttpClient].
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(
        apiKeyInterceptor: Interceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .addInterceptor(apiKeyInterceptor)
        .build()

    /**
     * Provides a [Retrofit] instance pointed at the Alpha Vantage base URL.
     */
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**
     * Provides a [MarketNewsApi] instance pointed at the Alpha Vantage base URL.
     */
    @Provides
    @Singleton
    fun provideMarketNewsApi(retrofit: Retrofit): MarketNewsApi =
        retrofit.create(MarketNewsApi::class.java)

    /**
     * Provides a [ExchangeRateApi] instance pointed at the Alpha Vantage base URL.
     */
    @Provides
    @Singleton
    fun provideExchangeRateApi(retrofit: Retrofit): ExchangeRateApi =
        retrofit.create(ExchangeRateApi::class.java)
}

