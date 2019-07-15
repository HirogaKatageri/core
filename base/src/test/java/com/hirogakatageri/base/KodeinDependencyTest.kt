package com.hirogakatageri.base

import com.hirogakatageri.base.test.TestApp
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(application = TestApp::class)
class KodeinDependencyTest {

    @Before
    fun prepare() {

    }

    @Test
    fun isKodeinDependencyInjected() {

    }

}