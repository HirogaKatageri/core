package dev.hirogakatageri.core.components

import android.view.ViewGroup
import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.R
import dev.hirogakatageri.core.databinding.CoreFragmentTestLayoutBinding
import dev.hirogakatageri.core.fragment.CoreFragment
import org.junit.Assert

class TestFragment : CoreFragment<CoreFragmentTestLayoutBinding>() {

    override fun createBinding(container: ViewGroup?): CoreFragmentTestLayoutBinding =
        CoreFragmentTestLayoutBinding.inflate(layoutInflater, container, false)

    override fun CoreFragmentTestLayoutBinding.bind() {
        fragmentTextView.text = "CoreFragment"
    }

    @SmallTest
    fun test_ViewBinding() {
        Assert.assertEquals(CoreFragmentTestLayoutBinding::class, binding!!::class)
        Assert.assertEquals(R.id.test_fragment_layout, binding?.root?.id)
        Assert.assertEquals(R.id.fragment_text_view, binding?.fragmentTextView?.id)
        Assert.assertEquals("CoreFragment", binding?.fragmentTextView?.text)

        binding {
            Assert.assertEquals(CoreFragmentTestLayoutBinding::class, this::class)
            Assert.assertEquals(R.id.test_fragment_layout, root.id)
            Assert.assertEquals(R.id.fragment_text_view, fragmentTextView.id)
            Assert.assertEquals("CoreFragment", fragmentTextView.text)
        }
    }

    @SmallTest
    fun test_UpdateViewBinding() {
        binding {
            fragmentTextView.text = "UpdateViewBinding"
        }

        Assert.assertEquals("UpdateViewBinding", binding?.fragmentTextView?.text)

        binding {
            Assert.assertEquals("UpdateViewBinding", fragmentTextView.text)
        }
    }
}
