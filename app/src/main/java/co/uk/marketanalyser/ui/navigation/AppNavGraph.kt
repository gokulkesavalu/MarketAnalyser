package co.uk.marketanalyser.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import co.uk.marketanalyser.ui.feature.exchangerates.ExchangeRateScreen
import co.uk.marketanalyser.ui.feature.exchangerates.ExchangeRateViewModel
import co.uk.marketanalyser.ui.feature.home.HomeScreen

@Composable
fun AppNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                onNavigateToExchangeRate = {
                    navController.navigate(Screen.ExchangeRate.route)
                }
            )
        }

        composable(route = Screen.ExchangeRate.route) {
            val viewModel: ExchangeRateViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            val onFromCurrencyChange: (String) -> Unit = remember(viewModel) { { viewModel.onFromCurrencyChange(it) } }
            val onToCurrencyChange: (String) -> Unit   = remember(viewModel) { { viewModel.onToCurrencyChange(it) } }
            val onFetchRate: () -> Unit                = remember(viewModel) { { viewModel.fetchExchangeRate() } }

            ExchangeRateScreen(
                uiState = uiState,
                onFromCurrencyChange = onFromCurrencyChange,
                onToCurrencyChange = onToCurrencyChange,
                onFetchRate = onFetchRate
            )
        }
    }
}
