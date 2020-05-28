package com.hirogakatageri.sandbox

import com.airbnb.epoxy.kotlinsample.helpers.ViewBindingKotlinModel
import com.hirogakatageri.sandbox.databinding.ItemSample1Binding
import java.util.*

data class TestText1EpoxyModel(
    val randomText: String = UUID.randomUUID().toString()
) : ViewBindingKotlinModel<ItemSample1Binding>(R.layout.item_sample1) {

    override fun ItemSample1Binding.bind() {
        textView.text = randomText
    }
}