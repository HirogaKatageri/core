package com.hirogakatageri.sandbox

import com.hirogakatageri.base.KodeinApplication
import org.kodein.di.Kodein
import org.kodein.di.android.x.androidXModule

class App : KodeinApplication() {

    override val kodein by Kodein.lazy {
        import(androidXModule(this@App))
    }

}