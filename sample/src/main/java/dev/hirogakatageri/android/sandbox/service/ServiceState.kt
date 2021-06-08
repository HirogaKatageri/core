package dev.hirogakatageri.android.sandbox.service

import androidx.camera.lifecycle.ProcessCameraProvider

sealed class ServiceState {

    abstract val state: Int

    data class Initialized(
        override val state: Int = 1
    ) : ServiceState()

    sealed class ProfileState : ServiceState() {

        data class Neutral(
            override val state: Int = 100
        ) : ProfileState()

        data class Movement(
            override val state: Int = 101,
            val x: Int,
            val y: Int,
        ) : ProfileState()

        data class Clicked(
            override val state: Int = 102
        ) : ProfileState()

        data class CameraProviderReady(
            override val state: Int = 103,
            val cameraProvider: ProcessCameraProvider
        ) : ProfileState()

    }

    sealed class CommandBarState : ServiceState() {

        data class Neutral(
            override val state: Int = 200
        ) : CommandBarState()

        data class Activated(
            override val state: Int = 201
        ) : CommandBarState()

    }

}