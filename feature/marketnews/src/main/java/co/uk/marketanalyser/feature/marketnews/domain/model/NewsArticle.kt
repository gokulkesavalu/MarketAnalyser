package co.uk.marketanalyser.feature.marketnews.domain.model

/**
 * Domain model for a single news article with sentiment data.
 *
 * @property title The headline of the article.
 * @property url The full URL to the article.
 * @property summary A brief summary of the article.
 * @property source The publication source (e.g., "Yahoo Finance").
 * @property timePublished Formatted publish time (e.g., "Apr 10, 2026 14:31").
 * @property overallSentimentLabel The overall sentiment label (e.g., "Bullish", "Neutral").
 * @property overallSentimentScore The numeric sentiment score.
 * @property topics List of topic labels associated with the article.
 * @property tickers List of ticker symbols mentioned in the article.
 */
data class NewsArticle(
    val title: String,
    val url: String,
    val summary: String,
    val source: String,
    val timePublished: String,
    val overallSentimentLabel: String,
    val overallSentimentScore: Double,
    val topics: List<NewsTopic>,
    val tickers: List<NewsTickerSentiment>
)

/**
 * Represents a topic associated with a news article.
 *
 * @property topic The name of the topic.
 * @property relevanceScore A score indicating the relevance of the topic to the article.
 */
data class NewsTopic(
    val topic: String,
    val relevanceScore: String
)

/**
 * Represents sentiment data for a specific stock ticker mentioned in a news article.
 *
 * @property ticker The stock ticker symbol (e.g., "AAPL").
 * @property relevanceScore How relevant the article is to this ticker.
 * @property tickerSentimentScore The sentiment score for this specific ticker.
 * @property tickerSentimentLabel The sentiment label for this specific ticker (e.g., "Bullish").
 */
data class NewsTickerSentiment(
    val ticker: String,
    val relevanceScore: String,
    val tickerSentimentScore: String,
    val tickerSentimentLabel: String
)

