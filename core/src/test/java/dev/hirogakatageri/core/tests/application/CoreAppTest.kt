package dev.hirogakatageri.core.tests.application

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.components.*
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinInternal
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules
import org.koin.test.get

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreAppTest : AutoCloseKoinTest() {

    @KoinInternal
    @Test
    fun test_Modules() {
        checkModules {
            modules(testMod)
        }
        getKoin().scopeRegistry.scopeDefinitions.let { map ->
            Assert.assertNotNull(map[TestActivity::class.qualifiedName])
            Assert.assertNotNull(map[TestViewModelActivity::class.qualifiedName])
            Assert.assertNotNull(map[TestFragment::class.qualifiedName])
            Assert.assertNotNull(map[TestViewModelFragment::class.qualifiedName])
        }
        Assert.assertNotNull(get<TestViewModel>())
    }

}