package com.hirogakatageri.core.tests.activity

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hirogakatageri.core.components.TestViewModelActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreViewModelActivityTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = ActivityScenarioRule(TestViewModelActivity::class.java)

    @After
    fun close() {
        rule.scenario.close()
    }

    @Test
    fun test_ViewModelInjection() {
        rule.scenario.onActivity { activity ->
            activity.test_ViewModelInjection()
        }
    }

}