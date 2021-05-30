package dev.hirogakatageri.android.sandbox.service

sealed class ServiceEvent {

    abstract val eventId: Int

    sealed class ProfileEvent : ServiceEvent() {

        data class ProfileClicked(override val eventId: Int = 101) : ProfileEvent()

        data class ProfileMoved(
            override val eventId: Int = 102,
            val x: Float,
            val y: Float,
            val width: Int,
            val height: Int
        ) : ProfileEvent()

    }

}
