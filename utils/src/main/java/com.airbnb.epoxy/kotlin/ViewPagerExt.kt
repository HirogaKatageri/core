package com.airbnb.epoxy.kotlin

import androidx.annotation.Keep
import androidx.viewpager2.widget.ViewPager2
import com.airbnb.epoxy.EpoxyController

@Keep
fun ViewPager2.setController(controller: EpoxyController) {
    adapter = controller.adapter
}

@Keep
fun ViewPager2.setControllerAndBuildModels(controller: EpoxyController) {
    setController(controller)
    controller.requestModelBuild()
}