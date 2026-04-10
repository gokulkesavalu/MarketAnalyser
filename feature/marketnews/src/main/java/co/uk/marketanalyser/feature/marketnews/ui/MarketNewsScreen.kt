package co.uk.marketanalyser.feature.marketnews.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import co.uk.marketanalyser.core.ui.theme.MarketAnalyserTheme
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsArticle

@Composable
fun MarketNewsScreen(
    uiState: MarketNewsUiState,
    onTickerChange: (String) -> Unit,
    onFetchNews: () -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Market News & Sentiment",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.ticker,
            onValueChange = onTickerChange,
            label = { Text("Ticker Symbol (e.g. AAPL)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onFetchNews()
                }
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onFetchNews()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Get News")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            uiState.articles.isNotEmpty() -> {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(uiState.articles, key = { it.url }) { article ->
                        NewsArticleCard(article = article)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NewsArticleCard(article: NewsArticle) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = article.title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${article.source}  ·  ${article.timePublished}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            SentimentChip(label = article.overallSentimentLabel)

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider()

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = article.summary,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            if (article.tickers.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                FlowRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    article.tickers.forEach { ticker ->
                        SuggestionChip(
                            onClick = {},
                            label = { Text(ticker, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SentimentChip(label: String) {
    val (containerColor, contentColor) = when {
        label.contains("Bullish", ignoreCase = true) -> Color(0xFF1B5E20) to Color.White
        label.contains("Bearish", ignoreCase = true) -> Color(0xFFB71C1C) to Color.White
        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }
    SuggestionChip(
        onClick = {},
        label = { Text(label, style = MaterialTheme.typography.labelSmall) },
        colors = SuggestionChipDefaults.suggestionChipColors(
            containerColor = containerColor,
            labelColor = contentColor
        )
    )
}

// ── Previews ──────────────────────────────────────────────────────────────────

private val sampleArticles = listOf(
    NewsArticle(
        title = "Apple leads global smartphone shipments in Q1 despite overall shipments declining",
        url = "https://seekingalpha.com/news/4574020",
        summary = "Apple led global smartphone shipments in the first quarter of 2026, experiencing a 5% year-over-year growth, even as overall market shipments declined due to memory component shortages and weak demand.",
        source = "Seeking Alpha",
        timePublished = "Apr 10, 2026 13:01",
        overallSentimentLabel = "Bullish",
        overallSentimentScore = 0.441002,
        topics = listOf("earnings", "technology"),
        tickers = listOf("AAPL")
    ),
    NewsArticle(
        title = "Apple will close its first unionized retail location in the US",
        url = "https://www.bloomberg.com/news/articles/2026-04-09/apple",
        summary = "Apple Inc. is closing its first unionized retail store, located in Towson, Maryland, citing the departure of other retailers and declining conditions at the mall.",
        source = "Bloomberg.com",
        timePublished = "Apr 09, 2026 23:19",
        overallSentimentLabel = "Somewhat-Bearish",
        overallSentimentScore = -0.180335,
        topics = listOf("retail_wholesale"),
        tickers = listOf("AAPL")
    )
)

@PreviewLightDark
@Composable
private fun PreviewEmpty() {
    MarketAnalyserTheme {
        MarketNewsScreen(
            uiState = MarketNewsUiState(),
            onTickerChange = {},
            onFetchNews = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoading() {
    MarketAnalyserTheme {
        MarketNewsScreen(
            uiState = MarketNewsUiState(ticker = "AAPL", isLoading = true),
            onTickerChange = {},
            onFetchNews = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewError() {
    MarketAnalyserTheme {
        MarketNewsScreen(
            uiState = MarketNewsUiState(ticker = "AAPL", error = "Network error. Please try again."),
            onTickerChange = {},
            onFetchNews = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewSuccess() {
    MarketAnalyserTheme {
        MarketNewsScreen(
            uiState = MarketNewsUiState(ticker = "AAPL", articles = sampleArticles),
            onTickerChange = {},
            onFetchNews = {}
        )
    }
}

