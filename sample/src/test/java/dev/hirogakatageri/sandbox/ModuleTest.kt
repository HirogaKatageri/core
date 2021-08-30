package dev.hirogakatageri.sandbox

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules
import org.koin.test.mock.MockProviderRule
import org.mockito.Mockito
import org.robolectric.annotation.Config

@SmallTest
@Config(manifest = Config.NONE)
@RunWith(AndroidJUnit4::class)
class ModuleTest : AutoCloseKoinTest() {

    @get:Rule
    val mockProvider = MockProviderRule.create { clazz ->
        Mockito.mock(clazz.java)
    }

    @Test
    fun verifyModules() = checkModules {
        modules(mainModule, viewServiceModule)
    }

}