package com.hirogakatageri.home

import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.hirogakatageri.home.HomeActivityMain
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

@RunWith(AndroidJUnit4::class)
class MainActivityInjectionTest : KoinTest {

    @get:Rule
    val activity = activityScenarioRule<HomeActivityMain>()

    @After
    fun onFinish() {
        stopKoin()
    }

    @Test
    fun test_Injection() {
        activity.scenario.moveToState(Lifecycle.State.CREATED)
        activity.scenario.onActivity {
            Assert.assertNotNull(it.viewModel)
        }
    }

}