package dev.hirogakatageri.sandbox.ui.feature

import dev.hirogakatageri.sandbox.R

class FeatureManager {

    enum class FeatureKey {
        CLOCK,
        OAUTH,
        VIEW_SERVICE,
        FCM,
        FIREBASE_AUTH,
        API_VERIFY_USER
    }

    val featureList: List<FeatureModel> = listOf(
        FeatureModel(FeatureKey.CLOCK, R.string.main_btn_clock),
        FeatureModel(FeatureKey.OAUTH, R.string.main_btn_oauth),
        FeatureModel(FeatureKey.VIEW_SERVICE, R.string.main_btn_view_service),
        FeatureModel(FeatureKey.FCM, R.string.main_btn_firebase_messaging),
        FeatureModel(FeatureKey.FIREBASE_AUTH, R.string.main_btn_firebase_auth),
        FeatureModel(FeatureKey.API_VERIFY_USER, R.string.main_btn_auth_verification)
    )
}
