package com.hirogakatageri.core.activity

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hirogakatageri.core.components.TestActivity
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreActivityTest {

    @get:Rule
    val rule = ActivityScenarioRule(TestActivity::class.java)

    @After
    fun close() {
        rule.scenario.close()
        stopKoin()
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