package dev.hirogakatageri.sandbox.ui.main.feature

import androidx.annotation.StringRes

data class FeatureModel(
    val key: FeatureManager.FeatureKey,
    @StringRes val titleRes: Int
)
