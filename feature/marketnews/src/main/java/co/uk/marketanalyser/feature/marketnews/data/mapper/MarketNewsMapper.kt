package co.uk.marketanalyser.feature.marketnews.data.mapper

import co.uk.marketanalyser.core.database.entity.MarketNewsEntity
import co.uk.marketanalyser.core.database.entity.MarketNewsTickerSentimentEntity
import co.uk.marketanalyser.core.database.entity.MarketNewsTopicEntity
import co.uk.marketanalyser.core.network.dto.MarketNewsItemDto
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsArticle
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsTickerSentiment
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsTopic

fun MarketNewsEntity.toDomainModel() = NewsArticle(
    title = title,
    url = url,
    summary = summary,
    source = source,
    timePublished = timePublished,
    overallSentimentLabel = overallSentimentLabel,
    overallSentimentScore = overallSentimentScore,
    topics = topics.map { NewsTopic(topic = it.topic, relevanceScore = it.relevanceScore) },
    tickers = tickerSentiments.map {
        NewsTickerSentiment(
            ticker = it.ticker,
            relevanceScore = it.relevanceScore,
            tickerSentimentScore = it.tickerSentimentScore,
            tickerSentimentLabel = it.tickerSentimentLabel
        )
    }
)

fun MarketNewsItemDto.toDomainModel() = NewsArticle(
    title = title,
    url = url,
    summary = summary,
    source = source,
    timePublished = timePublished,
    overallSentimentLabel = overallSentimentLabel,
    overallSentimentScore = overallSentimentScore,
    topics = topics.map { NewsTopic(topic = it.topic, relevanceScore = it.relevanceScore) },
    tickers = tickerSentiment.map {
        NewsTickerSentiment(
            ticker = it.ticker,
            relevanceScore = it.relevanceScore,
            tickerSentimentScore = it.tickerSentimentScore,
            tickerSentimentLabel = it.tickerSentimentLabel
        )
    }
)

fun MarketNewsItemDto.toEntity() = MarketNewsEntity(
    url = url,
    title = title,
    summary = summary,
    timePublished = timePublished,
    source = source,
    sourceDomain = sourceDomain,
    overallSentimentLabel = overallSentimentLabel,
    overallSentimentScore = overallSentimentScore,
    authors = authors,
    topics = topics.map {
        MarketNewsTopicEntity(
            topic = it.topic,
            relevanceScore = it.relevanceScore
        )
    },
    bannerImage = bannerImage,
    tickerSentiments = tickerSentiment.map {
        MarketNewsTickerSentimentEntity(
            ticker = it.ticker,
            relevanceScore = it.relevanceScore,
            tickerSentimentScore = it.tickerSentimentScore,
            tickerSentimentLabel = it.tickerSentimentLabel
        )
    }
)