package dev.hirogakatageri.core.tests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.R
import dev.hirogakatageri.core.components.ViewBindingTestActivity
import dev.hirogakatageri.core.databinding.CoreTestLayoutBinding
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreActivityTest : AutoCloseKoinTest() {

    @get:Rule
    val rule = ActivityScenarioRule(ViewBindingTestActivity::class.java)

    @After
    fun close() {
        rule.scenario.close()
    }

    @Test
    fun test_NormalViewBinding() {
        rule.scenario.onActivity { activity ->
            Assert.assertEquals(CoreTestLayoutBinding::class, activity.binding::class)
            Assert.assertEquals(R.id.test_layout, activity.binding.root.id)
            Assert.assertEquals(R.id.text_view, activity.binding.textView.id)
            Assert.assertEquals("Alpha", activity.binding.textView.text)
        }
    }

    @Test
    fun test_ShortViewBinding() {
        rule.scenario.onActivity { activity ->
            val bindingReturn = activity.binding {
                Assert.assertEquals(CoreTestLayoutBinding::class, this::class)
                Assert.assertEquals(R.id.test_layout, root.id)
                Assert.assertEquals(R.id.text_view, textView.id)
                Assert.assertEquals("Alpha", textView.text)
                textView.text
            }

            Assert.assertEquals("Alpha", bindingReturn)
        }
    }

    @Test
    fun test_NormalViewBindingUpdate() {
        rule.scenario.onActivity { activity ->
            val textView = activity.binding.textView
            val newText = "Beta"

            textView.text = newText

            Assert.assertEquals("Beta", activity.binding.textView.text)
        }
    }

    fun test_ShortViewBindingUpdate() {
        rule.scenario.onActivity { activity ->
            val newText = "Beta"

            val bindingReturn = activity.binding {
                textView.text = newText

                Assert.assertEquals("Beta", textView.text)
                newText
            }

            Assert.assertEquals("Beta", bindingReturn)
        }
    }
}
