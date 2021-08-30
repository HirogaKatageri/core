package dev.hirogakatageri.sandbox.ui.feature

import androidx.annotation.StringRes

data class FeatureModel(
    val key: FeatureManager.FeatureKey,
    @StringRes val titleRes: Int
)
