package dev.hirogakatageri.core.helpers.state

import androidx.annotation.Keep

@Keep
sealed class UiState<out T> {

    open val data: T? = null

    /**
     * This means the state of the UI is being initialized.
     * Use this to configure your initial UI state.
     * */
    @Keep
    data class Initializing<out T>(override val data: T? = null) : UiState<T>()

    /**
     * This means the state of the UI is being loaded.
     * Use these parameters to display an indefinite/definite progress indicator.
     * */
    @Keep
    data class Loading<out T>(
        override val data: T? = null,
        val messageResId: Int? = null,
        val isIndefinite: Boolean = true,
        val currentProgress: Long = 0,
        val maxProgress: Long = 100,
    ) : UiState<T>()

    /**
     * This means the state has finished loading successfully.
     * Use these parameters to display the state of the UI depending on the parameters.
     * */
    @Keep
    data class Success<out T>(
        override val data: T
    ) : UiState<T>()

    /**
     * This means the state has finished loading with an error.
     * Use these parameters to display the state of the UI depending on the parameters.
     * */
    @Keep
    data class Error<out T>(
        override val data: T? = null,
        val messageResId: Int? = null,
        val throwable: Throwable? = null,
    ) : UiState<T>()
}
