package com.hirogakatageri.sandbox

import org.lotustechnologies.epoxy_helper.ViewBindingKotlinModel
import com.hirogakatageri.sandbox.databinding.ItemSample2Binding
import java.util.*

data class TestText2EpoxyModel(
    val randomText: String = UUID.randomUUID().toString()
) : ViewBindingKotlinModel<ItemSample2Binding>(R.layout.item_sample2) {

    override fun ItemSample2Binding.bind() {}
}