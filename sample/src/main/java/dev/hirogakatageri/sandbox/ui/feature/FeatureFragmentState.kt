package dev.hirogakatageri.sandbox.ui.feature

import dev.hirogakatageri.sandbox.R

sealed class FeatureFragmentState {

    val state: String? get() = this::class.simpleName

    open val message: String? = null
    open val msgResId: Int? = null

    data class Default(
        override val message: String? = null,
        override val msgResId: Int? = null
    ) : FeatureFragmentState()

    data class UserSignedIn(
        val email: String,
        override val msgResId: Int = R.string.msg_user_signed_in
    ) : FeatureFragmentState()

    data class UserSignedOut(
        override val msgResId: Int = R.string.msg_user_signed_out
    ) : FeatureFragmentState()
}
