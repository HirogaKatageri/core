package dev.hirogakatageri.android.sandbox.ui.time

sealed class TimeFragmentState {

    abstract val state: Int
    abstract val time: String

    class Origin(
        override val state: Int = 0
    ) : TimeFragmentState() {
        override val time: String
            get() = "Origin State"
    }

    class TimeUpdated(
        override val state: Int = 1,
        override val time: String
    ) : TimeFragmentState()

}