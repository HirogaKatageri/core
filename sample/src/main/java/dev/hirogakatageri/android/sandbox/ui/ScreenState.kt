package dev.hirogakatageri.android.sandbox.ui

/**
 * States define the current scenario of the application.
 * For example MainScreen shows the MainFragment and TimeScreen shows the TimeFragment.
 *
 * In my practice, I declare the properties of states as immutable. Since if there should a change
 * there should also be a new state.
 * */
sealed class ScreenState {

    abstract val state: Int

    class MainScreen(override val state: Int = 0) : ScreenState()

    class TimeScreen(override val state: Int = 1) : ScreenState()

    class OAuthScreen(override val state: Int = 2) : ScreenState()

}
