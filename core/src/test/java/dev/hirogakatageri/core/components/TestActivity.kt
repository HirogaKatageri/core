package dev.hirogakatageri.core.components

import androidx.test.filters.SmallTest
import dev.hirogakatageri.core.R
import dev.hirogakatageri.core.activity.CoreActivity
import dev.hirogakatageri.core.databinding.CoreTestLayoutBinding
import org.junit.Assert

class TestActivity : CoreActivity<CoreTestLayoutBinding>() {

    override fun createBinding(): CoreTestLayoutBinding =
        CoreTestLayoutBinding.inflate(layoutInflater)

    override fun CoreTestLayoutBinding.bind() {
        textView.text = "CoreActivityTest"
    }

    @SmallTest
    fun test_ViewBinding() {
        Assert.assertEquals(CoreTestLayoutBinding::class, binding::class)
        Assert.assertEquals(R.id.test_layout, binding.root.id)
        Assert.assertEquals(R.id.text_view, binding.textView.id)
        Assert.assertEquals("CoreActivityTest", binding.textView.text)

        binding {
            Assert.assertEquals(CoreTestLayoutBinding::class, this::class)
            Assert.assertEquals(R.id.test_layout, root.id)
            Assert.assertEquals(R.id.text_view, textView.id)
            Assert.assertEquals("CoreActivityTest", textView.text)
        }
    }

    @SmallTest
    fun test_UpdateViewBinding() {
        binding {
            textView.text = "UpdateViewBinding"
        }

        Assert.assertEquals("UpdateViewBinding", binding.textView.text)

        binding {
            Assert.assertEquals("UpdateViewBinding", textView.text)
        }
    }
}
