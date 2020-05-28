package com.hirogakatageri.sandbox

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.EpoxyModel

class TestController : EpoxyController() {

    var models = emptyList<EpoxyModel<*>>()

    override fun buildModels() {
        models.forEach { it.addTo(this) }
    }
}