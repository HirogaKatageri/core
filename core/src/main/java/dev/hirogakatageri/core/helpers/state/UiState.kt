package dev.hirogakatageri.core.helpers.state

import androidx.annotation.Keep

@Keep
sealed class UiState {

    @Keep
    object Neutral : UiState()

    @Keep
    data class Loading(
        val message: String = "",
        val isIndefinite: Boolean = true,
        val currentProgress: Long = 0,
        val maxProgress: Long = 0,
    ) : UiState()

    @Keep
    data class Success(
        val message: String = "",
    ) : UiState()

    @Keep
    data class Error(
        val message: String = "",
    ) : UiState()
}

