package com.hirogakatageri.sandbox

import android.app.Application
import androidx.fragment.app.Fragment
import com.hirogakatageri.sandbox.ui.main.FeaturesFragment
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.with

class App : Application(), KodeinAware {

    companion object {
        const val FRAGMENTS = "fragments"
        const val FEATURES = "features"
    }

    override val kodein: Kodein by Kodein.lazy {
        constant(tag = FRAGMENTS) with listOf<Fragment>(FeaturesFragment())
        constant(tag = FEATURES) with listOf("Chat")
    }
}