package com.hirogakatageri.base.test

import com.hirogakatageri.base.KodeinApplication
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

class TestApp : KodeinApplication() {

    override val kodein by Kodein.lazy {
        bind<TestData>() with provider { TestData("Sample") }
    }
}