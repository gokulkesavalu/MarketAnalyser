package co.uk.marketanalyser.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a market news article.
 *
 * This entity stores news data locally to support offline-first functionality.
 * Nested lists like [authors], [topics], and [tickerSentiments] are stored as JSON
 * strings in the database using Room TypeConverters.
 *
 * @property url The unique identifier (URL) of the news article.
 * @property title The title of the article.
 * @property summary A brief summary of the article content.
 * @property timePublished The timestamp when the article was published.
 * @property bannerImage URL to the article's banner image, if available.
 * @property source The name of the news source (e.g., "Reuters").
 * @property sourceDomain The domain of the news source.
 * @property overallSentimentScore The calculated overall sentiment score for the article.
 * @property overallSentimentLabel The human-readable sentiment label (e.g., "Bullish").
 * @property authors List of authors who wrote the article.
 * @property topics List of associated topics and their relevance scores.
 * @property tickerSentiments List of specific stock tickers mentioned and their sentiment data.
 * @property cachedAt The timestamp when this record was saved to the local database, used for TTL logic.
 */
@Entity(tableName = "market_news")
data class MarketNewsEntity(
    @PrimaryKey val url: String,
    val title: String,
    val summary: String,
    val timePublished: String,
    val bannerImage: String?,
    val source: String,
    val sourceDomain: String,
    val overallSentimentScore: Double,
    val overallSentimentLabel: String,

    // These lists will be handled by TypeConverters
    val authors: List<String>,
    val topics: List<MarketNewsTopicEntity>,
    val tickerSentiments: List<MarketNewsTickerSentimentEntity>,

    val cachedAt: Long = System.currentTimeMillis()
)

/**
 * Represents a topic associated with a market news article.
 *
 * This is used as part of a list within [MarketNewsEntity].
 *
 * @property topic The name of the topic.
 * @property relevanceScore The relevance score of the topic to the article.
 */
data class MarketNewsTopicEntity(
    val topic: String,
    val relevanceScore: String
)

/**
 * Represents sentiment data for a specific stock ticker mentioned in a news article.
 *
 * This is used as part of a list within [MarketNewsEntity].
 *
 * @property ticker The stock ticker symbol (e.g., "AAPL").
 * @property relevanceScore How relevant the article is to this ticker.
 * @property tickerSentimentScore The sentiment score for this specific ticker.
 * @property tickerSentimentLabel The sentiment label for this specific ticker.
 */
data class MarketNewsTickerSentimentEntity(
    val ticker: String,
    val relevanceScore: String,
    val tickerSentimentScore: String,
    val tickerSentimentLabel: String
)
