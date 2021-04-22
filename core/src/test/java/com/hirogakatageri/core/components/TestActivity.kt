package com.hirogakatageri.core.components

import androidx.test.filters.SmallTest
import com.hirogakatageri.core.R
import com.hirogakatageri.core.activity.CoreActivity
import com.hirogakatageri.core.databinding.CoreTestLayoutBinding
import org.junit.Assert

class TestActivity : CoreActivity<CoreTestLayoutBinding>() {

    override fun createBinding(): CoreTestLayoutBinding =
        CoreTestLayoutBinding.inflate(layoutInflater)

    override fun CoreTestLayoutBinding.bind() {
        textView.text = "CoreActivityTest"
    }

    @SmallTest
    fun test_ViewBinding() {
        Assert.assertTrue(binding is CoreTestLayoutBinding)
        Assert.assertEquals(R.id.test_layout, binding.root.id)
        Assert.assertEquals(R.id.text_view, binding.textView.id)
        Assert.assertEquals("CoreActivityTest", binding.textView.text)
    }

    @SmallTest
    fun test_UpdateViewBinding() {
        binding {
            textView.text = "UpdateViewBinding"
        }

        Assert.assertEquals("UpdateViewBinding", binding.textView.text)
    }

}