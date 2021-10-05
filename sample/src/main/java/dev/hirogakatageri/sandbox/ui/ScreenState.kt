package dev.hirogakatageri.sandbox.ui

/**
 * States define the current scenario of the application.
 * For example MainScreen shows the MainFragment and TimeScreen shows the TimeFragment.
 *
 * In my practice, I declare the properties of states as immutable.
 * If there is a change to the state there should be a new state.
 * */
sealed class ScreenState {

    open val state: String?
        get() = this::class.simpleName

    data class MainScreen(override val state: String = "MainScreen") : ScreenState()

    data class TimeScreen(override val state: String = "TimeScreen") : ScreenState()

    data class OAuthScreen(override val state: String = "OAuthScreen") : ScreenState()

    data class FCMScreen(
        val message: String
    ) : ScreenState()

    data class ChatScreen(
        override val state: String = "ChatScreen"
    ) : ScreenState()
}
