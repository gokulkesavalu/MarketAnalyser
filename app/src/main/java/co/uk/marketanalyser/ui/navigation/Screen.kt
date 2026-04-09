package co.uk.marketanalyser.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object ExchangeRate : Screen("exchange_rate")
}

