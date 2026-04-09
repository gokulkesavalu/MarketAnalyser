package co.uk.marketanalyser.ui.feature.exchangerates

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import co.uk.marketanalyser.domain.model.ExchangeRate
import co.uk.marketanalyser.ui.theme.MarketAnalyserTheme

@Composable
fun ExchangeRateScreen(
    uiState: ExchangeRateUiState,
    onFromCurrencyChange: (String) -> Unit,
    onToCurrencyChange: (String) -> Unit,
    onFetchRate: () -> Unit,
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
            text = "Currency Exchange",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = uiState.fromCurrency,
            onValueChange = onFromCurrencyChange,
            label = { Text("From Currency (e.g. USD)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = uiState.toCurrency,
            onValueChange = onToCurrencyChange,
            label = { Text("To Currency (e.g. JPY)") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                    onFetchRate()
                }
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                focusManager.clearFocus()
                onFetchRate()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading
        ) {
            Text("Get Exchange Rate")
        }

        Spacer(modifier = Modifier.height(24.dp))

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

            uiState.exchangeRate != null -> {
                ExchangeRateResult(rate = uiState.exchangeRate)
            }
        }
    }
}

@Composable
private fun ExchangeRateResult(rate: ExchangeRate) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${rate.fromCurrencyCode} → ${rate.toCurrencyCode}",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "${rate.fromCurrencyName} to ${rate.toCurrencyName}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))

            RateRow(label = "Exchange Rate", value = "%.6f".format(rate.exchangeRate))
            RateRow(label = "Bid Price", value = "%.6f".format(rate.bidPrice))
            RateRow(label = "Ask Price", value = "%.6f".format(rate.askPrice))

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Last updated: ${rate.lastRefreshed} (${rate.timeZone})",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun RateRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ── Previews ──────────────────────────────────────────────────────────────────

@PreviewLightDark
@Composable
private fun PreviewEmpty() {
    MarketAnalyserTheme {
        ExchangeRateScreen(
            uiState = ExchangeRateUiState(),
            onFromCurrencyChange = {},
            onToCurrencyChange = {},
            onFetchRate = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewLoading() {
    MarketAnalyserTheme {
        ExchangeRateScreen(
            uiState = ExchangeRateUiState(
                fromCurrency = "USD",
                toCurrency = "JPY",
                isLoading = true
            ),
            onFromCurrencyChange = {},
            onToCurrencyChange = {},
            onFetchRate = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewError() {
    MarketAnalyserTheme {
        ExchangeRateScreen(
            uiState = ExchangeRateUiState(
                fromCurrency = "USD",
                toCurrency = "JPY",
                error = "Network error. Please try again."
            ),
            onFromCurrencyChange = {},
            onToCurrencyChange = {},
            onFetchRate = {}
        )
    }
}

@PreviewLightDark
@Composable
private fun PreviewSuccess() {
    MarketAnalyserTheme {
        ExchangeRateScreen(
            uiState = ExchangeRateUiState(
                fromCurrency = "USD",
                toCurrency = "JPY",
                exchangeRate = ExchangeRate(
                    fromCurrencyCode = "USD",
                    fromCurrencyName = "United States Dollar",
                    toCurrencyCode = "JPY",
                    toCurrencyName = "Japanese Yen",
                    exchangeRate = 158.74988722,
                    lastRefreshed = "2026-04-09 17:49:38",
                    timeZone = "UTC",
                    bidPrice = 158.74831560,
                    askPrice = 158.75695953
                )
            ),
            onFromCurrencyChange = {},
            onToCurrencyChange = {},
            onFetchRate = {}
        )
    }
}
