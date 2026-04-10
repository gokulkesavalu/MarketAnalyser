package co.uk.marketanalyser.ui.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import co.uk.marketanalyser.core.ui.theme.MarketAnalyserTheme

/**
 * The entry point screen of the application.
 *
 * @param onNavigateToExchangeRate Callback to navigate to the Exchange Rate screen.
 * @param onNavigateToMarketNews Callback to navigate to the Market News screen.
 * @param modifier The [Modifier] to be applied to the layout.
 */
@Composable
fun HomeScreen(
    onNavigateToExchangeRate: () -> Unit,
    onNavigateToMarketNews: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Market Analyser",
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Real-time market data at your fingertips",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onNavigateToExchangeRate,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Check Exchange Rate")
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onNavigateToMarketNews,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Market News & Sentiment")
        }
    }
}

@PreviewLightDark
@Composable
private fun HomeScreenPreview() {
    MarketAnalyserTheme {
        HomeScreen(
            onNavigateToExchangeRate = {},
            onNavigateToMarketNews = {}
        )
    }
}
