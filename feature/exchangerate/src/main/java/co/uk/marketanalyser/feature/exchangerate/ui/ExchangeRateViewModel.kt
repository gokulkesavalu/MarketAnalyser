package co.uk.marketanalyser.feature.exchangerate.ui

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.uk.marketanalyser.feature.exchangerate.domain.model.ExchangeRate
import co.uk.marketanalyser.feature.exchangerate.domain.repository.ExchangeRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for the Exchange Rate screen.
 */
@HiltViewModel
class ExchangeRateViewModel @Inject constructor(
    private val repository: ExchangeRateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeRateUiState())
    val uiState: StateFlow<ExchangeRateUiState> = _uiState.asStateFlow()

    fun onFromCurrencyChange(value: String) {
        _uiState.update { it.copy(fromCurrency = value.uppercase(), error = null) }
    }

    fun onToCurrencyChange(value: String) {
        _uiState.update { it.copy(toCurrency = value.uppercase(), error = null) }
    }

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
                        it.copy(isLoading = false, error = error.message ?: "Unknown error")
                    }
                }
        }
    }
}

@Immutable
data class ExchangeRateUiState(
    val fromCurrency: String = "",
    val toCurrency: String = "",
    val exchangeRate: ExchangeRate? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

