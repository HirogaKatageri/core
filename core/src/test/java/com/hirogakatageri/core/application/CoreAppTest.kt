package com.hirogakatageri.core.application

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hirogakatageri.core.components.TestActivity
import com.hirogakatageri.core.components.testMod
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinInternal
import org.koin.test.AutoCloseKoinTest
import org.koin.test.check.checkModules

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
        }
    }

}