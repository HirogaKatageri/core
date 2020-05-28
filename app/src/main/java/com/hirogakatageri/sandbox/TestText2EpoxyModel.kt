package com.hirogakatageri.sandbox

import android.view.View
import android.widget.TextView
import com.airbnb.epoxy.kotlinsample.helpers.KotlinModel
import com.airbnb.epoxy.kotlinsample.helpers.ViewBindingKotlinModel
import com.hirogakatageri.sandbox.databinding.ItemSample2Binding
import java.util.*

data class TestText2EpoxyModel(
    val randomText: String = UUID.randomUUID().toString()
) : ViewBindingKotlinModel<ItemSample2Binding>(R.layout.item_sample2) {

    override fun ItemSample2Binding.bind() {}
}