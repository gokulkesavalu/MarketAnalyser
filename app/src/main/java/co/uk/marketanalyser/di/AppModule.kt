package co.uk.marketanalyser.di

import co.uk.marketanalyser.BuildConfig
import co.uk.marketanalyser.data.repository.ExchangeRateRepositoryImpl
import co.uk.marketanalyser.data.api.ExchangeRateApi
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

/**
 * Hilt module for providing application-wide dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    /**
     * Binds the [ExchangeRateRepositoryImpl] to its interface [ExchangeRateRepository].
     *
     * @param impl The implementation of the repository.
     * @return The repository interface.
     */
    @Binds
    @Singleton
    abstract fun bindExchangeRateRepository(
        impl: ExchangeRateRepositoryImpl
    ): ExchangeRateRepository

    companion object {

        /**
         * Provides an [Interceptor] to automatically add the API key to all requests.
         *
         * @return The API key interceptor.
         */
        @Provides
        @Singleton
        fun provideApiKeyInterceptor(): Interceptor = Interceptor { chain ->
            val request = chain.request()
            val url = request.url.newBuilder()
                .addQueryParameter("apikey", BuildConfig.ALPHA_VANTAGE_API_KEY)
                .build()
            chain.proceed(request.newBuilder().url(url).build())
        }

        /**
         * Provides a [HttpLoggingInterceptor] for logging network requests and responses.
         *
         * @return The logging interceptor.
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
         *
         * @param apiKeyInterceptor The interceptor that adds the API key.
         * @param loggingInterceptor The interceptor that logs network traffic.
         * @return A configured OkHttp client.
         */
        @Provides
        @Singleton
        fun provideOkHttpClient(
            apiKeyInterceptor: Interceptor,
            loggingInterceptor: HttpLoggingInterceptor
        ): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()

        /**
         * Provides a [Retrofit] instance.
         *
         * @param okHttpClient The OkHttp client to use for network requests.
         * @return A configured Retrofit instance.
         */
        @Provides
        @Singleton
        fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        /**
         * Provides the [ExchangeRateApi] service.
         *
         * @param retrofit The Retrofit instance.
         */
        @Provides
        @Singleton
        fun provideExchangeRateApi(retrofit: Retrofit): ExchangeRateApi =
            retrofit.create(ExchangeRateApi::class.java)
    }
}
