package dev.hirogakatageri.core.tests

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.CoreApp
import dev.hirogakatageri.core.components.TestApp
import dev.hirogakatageri.core.components.ViewBindingTestActivity
import dev.hirogakatageri.core.components.ViewBindingTestFragment
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.annotation.KoinInternalApi
import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.TypeQualifier
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreAppTest : AutoCloseKoinTest() {

    private lateinit var _app: Application

    @Before
    fun setup() {
        _app = ApplicationProvider.getApplicationContext()
    }

    @Test
    fun test_ApplicationType() {
        Assert.assertNotNull(_app)
        Assert.assertTrue(_app is CoreApp)
        Assert.assertTrue(_app is TestApp)
        Assert.assertEquals("TestApp", _app::class.simpleName)
    }

    @KoinInternalApi
    @Test
    fun test_Scope() {
        val app = _app as TestApp
        val scopes = setOf<Qualifier>(
            TypeQualifier(ViewBindingTestActivity::class),
            TypeQualifier(ViewBindingTestFragment::class),
        )
        Assert.assertTrue(getKoin().scopeRegistry.scopeDefinitions.containsAll(scopes))
    }
}
