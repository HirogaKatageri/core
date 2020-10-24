package com.hirogakatageri.sandbox.utils

import android.view.View
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.kotlin.ViewBindingHolder


interface GenericEpoxyViewBindingClickListener {

    fun onClickEpoxyModel(
        model: EpoxyModel<*>,
        parentView: ViewBindingHolder,
        clickedView: View,
        position: Int
    )

}