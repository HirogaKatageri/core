package dev.hirogakatageri.core.tests.fragment

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragment
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.components.TestViewModelFragment
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreViewModelFragmentTest : AutoCloseKoinTest() {

    lateinit var scenario: FragmentScenario<TestViewModelFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer { TestViewModelFragment() }
    }

    @Test
    fun test_ViewModelInjection() {
        scenario.onFragment { fragment ->
            fragment.test_ViewModelInjection()
        }
    }
}
