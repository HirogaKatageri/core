package dev.hirogakatageri.sandbox.ui

/**
 * States define the current scenario of the application.
 * For example MainScreen shows the MainFragment and TimeScreen shows the TimeFragment.
 *
 * In my practice, I declare the properties of states as immutable.
 * If there is a change to the state there should be a new state.
 * */
sealed class ScreenState {

    abstract val state: Int

    data class MainScreen(override val state: Int = 0) : ScreenState()

    data class TimeScreen(override val state: Int = 1) : ScreenState()

    data class OAuthScreen(override val state: Int = 2) : ScreenState()

    data class FCMScreen(
        override val state: Int = 3,
        val message: String
    ) : ScreenState()
}
