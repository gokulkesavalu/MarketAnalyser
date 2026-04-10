package co.uk.marketanalyser.core.network.api

import co.uk.marketanalyser.core.network.dto.MarketNewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MarketNewsApi {
    /**
     * Fetches market news and sentiment for a given ticker or comma-separated list of tickers.
     *
     * @param function The API function (default: "NEWS_SENTIMENT").
     * @param tickers A ticker symbol or comma-separated list (e.g., "AAPL" or "AAPL,MSFT").
     * @return [MarketNewsResponse] containing the sentiment feed.
     */
    @GET("query")
    suspend fun getMarketNews(
        @Query("function") function: String = "NEWS_SENTIMENT",
        @Query("tickers") tickers: String
    ): MarketNewsResponse
}