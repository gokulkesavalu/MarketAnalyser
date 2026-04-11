package co.uk.marketanalyser.feature.marketnews.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.marketanalyser.feature.marketnews.domain.model.NewsArticle
import co.uk.marketanalyser.feature.marketnews.domain.repository.MarketNewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Market News screen.
 */
@HiltViewModel
class MarketNewsViewModel @Inject constructor(
    private val repository: MarketNewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MarketNewsUiState())
    val uiState: StateFlow<MarketNewsUiState> = _uiState.asStateFlow()

    fun onTickerChange(value: String) {
        _uiState.update { it.copy(ticker = value.uppercase(), error = null) }
    }

    fun fetchNews() {
        val ticker = _uiState.value.ticker.trim()
        if (ticker.isBlank()) {
            _uiState.update { it.copy(error = "Please enter a ticker symbol (e.g. AAPL)") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, articles = emptyList()) }
            repository.getMarketNews(ticker)
                .onSuccess { articles ->
                    _uiState.update { it.copy(isLoading = false, articles = articles) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, error = error.message ?: "Unknown error")
                    }
                }
        }
    }
}

@Immutable
data class MarketNewsUiState(
    val ticker: String = "",
    val articles: List<NewsArticle> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

