package co.uk.marketanalyser.ui.feature.exchangerates

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.marketanalyser.domain.repository.ExchangeRateRepository
import co.uk.marketanalyser.domain.model.ExchangeRate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Exchange Rate screen.
 * Handles fetching exchange rates and managing UI state.
 *
 * @property repository The repository to fetch exchange rate data.
 */
@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val repository: ExchangeRateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeRateUiState())

    /**
     * The current state of the Exchange Rate UI.
     */
    val uiState: StateFlow<ExchangeRateUiState> = _uiState.asStateFlow()

    /**
     * Updates the base currency code in the state.
     *
     * @param value The new base currency code (e.g., "USD").
     */
    fun onFromCurrencyChange(value: String) {
        _uiState.update { it.copy(fromCurrency = value.uppercase(), error = null) }
    }

    /**
     * Updates the target currency code in the state.
     *
     * @param value The new target currency code (e.g., "GBP").
     */
    fun onToCurrencyChange(value: String) {
        _uiState.update { it.copy(toCurrency = value.uppercase(), error = null) }
    }

    /**
     * Fetches the exchange rate based on the current UI state.
     */
    fun fetchExchangeRate() {
        val state = _uiState.value
        if (state.fromCurrency.isBlank() || state.toCurrency.isBlank()) {
            _uiState.update { it.copy(error = "Please enter both currencies") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, exchangeRate = null) }
            repository.getExchangeRate(state.fromCurrency, state.toCurrency)
                .onSuccess { rate ->
                    _uiState.update { it.copy(isLoading = false, exchangeRate = rate) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = error.message ?: "Unknown error"
                        )
                    }
                }
        }
    }
}

/**
 * Represents the UI state for the Exchange Rate screen.
 *
 * @property fromCurrency The base currency code.
 * @property toCurrency The target currency code.
 * @property exchangeRate The fetched exchange rate data, or null if not yet fetched.
 * @property isLoading Whether a network request is currently in progress.
 * @property error An error message to display, or null if there's no error.
 */
@Immutable
data class ExchangeRateUiState(
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val exchangeRate: ExchangeRate? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

