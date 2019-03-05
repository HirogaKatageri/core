package com.hirogakatageri.sandbox.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.hirogakatageri.sandbox.App
import com.hirogakatageri.sandbox.base.BaseViewModel
import org.kodein.di.generic.instance

class FeaturesViewModel(app: Application) : BaseViewModel(app) {

    private val features by instance<List<String>>(tag = App.FEATURES)

    val liveFragmentFeatures = MutableLiveData<List<String>>()

    fun getFeatures() {
        liveFragmentFeatures.value = features
    }

}