package dev.hirogakatageri.core.tests.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.components.TestFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreFragmentTest : AutoCloseKoinTest() {

    lateinit var scenario: FragmentScenario<TestFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer { TestFragment() }
    }

    @Test
    fun test_ViewBinding() {
        scenario.onFragment { fragment ->
            fragment.test_ViewBinding()
        }
    }

    @Test
    fun test_UpdateViewBinding() {
        scenario.onFragment { fragment ->
            fragment.test_UpdateViewBinding()
        }
    }

}