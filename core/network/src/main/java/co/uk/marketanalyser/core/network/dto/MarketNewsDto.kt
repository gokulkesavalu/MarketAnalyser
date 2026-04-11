package co.uk.marketanalyser.core.network.dto

import com.google.gson.annotations.SerializedName

/**
 * Root response for the Alpha Vantage NEWS_SENTIMENT API.
 */
data class MarketNewsResponse(
    @SerializedName("items")
    val items: String,

    @SerializedName("sentiment_score_definition")
    val sentimentScoreDefinition: String,

    @SerializedName("relevance_score_definition")
    val relevanceScoreDefinition: String,

    @SerializedName("feed")
    val feed: List<MarketNewsItemDto>
)

/**
 * A single news article from the sentiment feed.
 */
data class MarketNewsItemDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("url")
    val url: String,

    @SerializedName("time_published")
    val timePublished: String,

    @SerializedName("authors")
    val authors: List<String>,

    @SerializedName("summary")
    val summary: String,

    @SerializedName("banner_image")
    val bannerImage: String?,

    @SerializedName("source")
    val source: String,

    @SerializedName("source_domain")
    val sourceDomain: String,

    @SerializedName("topics")
    val topics: List<TopicDto>,

    @SerializedName("overall_sentiment_score")
    val overallSentimentScore: Double,

    @SerializedName("overall_sentiment_label")
    val overallSentimentLabel: String,

    @SerializedName("ticker_sentiment")
    val tickerSentiment: List<TickerSentimentDto>
)

/**
 * A topic tag associated with a news article.
 */
data class TopicDto(
    @SerializedName("topic")
    val topic: String,

    @SerializedName("relevance_score")
    val relevanceScore: String
)

/**
 * Sentiment data for a specific ticker in a news article.
 */
data class TickerSentimentDto(
    @SerializedName("ticker")
    val ticker: String,

    @SerializedName("relevance_score")
    val relevanceScore: String,

    @SerializedName("ticker_sentiment_score")
    val tickerSentimentScore: String,

    @SerializedName("ticker_sentiment_label")
    val tickerSentimentLabel: String
)

