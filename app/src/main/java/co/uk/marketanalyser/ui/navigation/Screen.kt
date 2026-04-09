package co.uk.marketanalyser.ui.navigation

/**
 * Represents the different screens/destinations in the application's navigation.
 * The [route] is automatically derived from the class name — no hardcoded strings.
 */
sealed class Screen {
    val route: String get() = this::class.simpleName!!

    /**
     * The initial home screen of the application.
     */
    data object Home : Screen()

    /**
     * The screen for viewing and calculating currency exchange rates.
     */
    data object ExchangeRate : Screen()
}
