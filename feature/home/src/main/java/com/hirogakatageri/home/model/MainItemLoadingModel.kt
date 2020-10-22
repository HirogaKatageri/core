package com.hirogakatageri.home.model

import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.kotlin.ViewBindingEpoxyModelWithHolder
import com.hirogakatageri.home.R
import com.hirogakatageri.home.databinding.HomeMainItemLoadingBinding

@EpoxyModelClass
abstract class MainItemLoadingModel :
    ViewBindingEpoxyModelWithHolder<HomeMainItemLoadingBinding>() {

    override fun HomeMainItemLoadingBinding.bind() {}

    override fun getDefaultLayout(): Int = R.layout.home_main_item_loading
}