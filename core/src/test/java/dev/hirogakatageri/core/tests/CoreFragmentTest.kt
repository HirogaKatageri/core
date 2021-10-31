package dev.hirogakatageri.core.tests

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.R
import dev.hirogakatageri.core.components.ViewBindingTestFragment
import dev.hirogakatageri.core.databinding.CoreFragmentTestLayoutBinding
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.AutoCloseKoinTest

@SmallTest
@RunWith(AndroidJUnit4::class)
class CoreFragmentTest : AutoCloseKoinTest() {

    lateinit var scenario: FragmentScenario<ViewBindingTestFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer { ViewBindingTestFragment() }
    }

    @Test
    fun test_NormalViewBinding() {
        scenario.onFragment { fragment ->
            Assert.assertNotNull(fragment.binding)
            Assert.assertEquals(CoreFragmentTestLayoutBinding::class, fragment.binding!!::class)
            Assert.assertEquals(R.id.test_fragment_layout, fragment.binding?.root?.id)
            Assert.assertEquals(R.id.fragment_text_view, fragment.binding?.fragmentTextView?.id)
            Assert.assertEquals("Alpha", fragment.binding?.fragmentTextView?.text)
        }
    }

    @Test
    fun test_ShortViewBinding() {
        scenario.onFragment { fragment ->
            val bindingReturn = fragment.binding {
                Assert.assertEquals(CoreFragmentTestLayoutBinding::class, this::class)
                Assert.assertEquals(R.id.test_fragment_layout, root.id)
                Assert.assertEquals(R.id.fragment_text_view, fragmentTextView.id)
                Assert.assertEquals("Alpha", fragmentTextView.text)

                fragmentTextView.text
            }

            Assert.assertEquals("Alpha", bindingReturn)
        }
    }

    @Test
    fun test_NormalViewBindingUpdate() {
        scenario.onFragment { fragment ->
            val textView = fragment.binding?.fragmentTextView
            val newText = "Beta"

            textView?.text = newText

            Assert.assertEquals("Beta", textView?.text)
        }
    }

    @Test
    fun test_ShortViewBindingUpdate() {
        scenario.onFragment { fragment ->
            val bindingReturn = fragment.binding {
                fragmentTextView.text = "Beta"

                Assert.assertEquals("Beta", fragmentTextView.text)
                fragmentTextView.text
            }

            Assert.assertEquals("Beta", bindingReturn)
        }
    }

    @Test
    fun test_DestroyBehavior() {
        scenario.onFragment { fragment ->
            scenario.moveToState(Lifecycle.State.DESTROYED)
            Assert.assertNull(fragment.binding)
        }
    }
}
