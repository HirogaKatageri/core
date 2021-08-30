package dev.hirogakatageri.sandbox.ui.fcm

sealed class FcmFragmentState {

    abstract val state: Int
    abstract val message: String

    data class Neutral(
        override val state: Int = 0
    ) : FcmFragmentState() {
        override val message: String = ""
    }

    data class NotificationReceived(
        override val state: Int = 1,
        override val message: String
    ) : FcmFragmentState()
}
