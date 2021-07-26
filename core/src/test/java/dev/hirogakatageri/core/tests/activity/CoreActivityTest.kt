package dev.hirogakatageri.core.tests.activity

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.components.TestActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreActivityTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = ActivityScenarioRule(TestActivity::class.java)

    @After
    fun close() {
        rule.scenario.close()
    }

    @Test
    fun test_ViewBinding() {
        rule.scenario.onActivity { activity ->
            activity.test_ViewBinding()
        }
    }

    @Test
    fun test_UpdateViewBinding() {
        rule.scenario.onActivity { activity ->
            activity.test_UpdateViewBinding()
        }
    }
}
