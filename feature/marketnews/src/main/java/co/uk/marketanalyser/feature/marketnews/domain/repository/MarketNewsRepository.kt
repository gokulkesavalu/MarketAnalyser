package co.uk.marketanalyser.feature.marketnews.domain.repository

import co.uk.marketanalyser.feature.marketnews.domain.model.NewsArticle

/**
 * Repository interface for market news and sentiment data.
 */
interface MarketNewsRepository {

    /**
     * Fetches market news and sentiment for the given ticker(s).
     *
     * @param tickers A ticker symbol or comma-separated list (e.g., "AAPL" or "AAPL,MSFT").
     * @return A [Result] containing a list of [NewsArticle] on success, or a failure exception.
     */
    suspend fun getMarketNews(tickers: String): Result<List<NewsArticle>>
}

