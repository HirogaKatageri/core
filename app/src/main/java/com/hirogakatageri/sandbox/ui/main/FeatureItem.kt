package com.hirogakatageri.sandbox.ui.main

import com.hirogakatageri.sandbox.base.BaseItem

data class FeatureItem(
    override val type: Int = TYPE_NORMAL,
    val featureName: String = ""
) : BaseItem {

    companion object {
        const val TYPE_NORMAL = 0
    }

}