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
    val topics: List<String>,
    val tickers: List<String>
)

