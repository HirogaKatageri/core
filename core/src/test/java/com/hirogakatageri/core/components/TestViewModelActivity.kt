package com.hirogakatageri.core.components

import androidx.test.filters.SmallTest
import com.hirogakatageri.core.activity.CoreViewModelActivity
import com.hirogakatageri.core.databinding.CoreTestLayoutBinding
import org.junit.Assert
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestViewModelActivity : CoreViewModelActivity<CoreTestLayoutBinding, TestViewModel>() {

    override val vm: TestViewModel by viewModel()

    override fun createBinding(): CoreTestLayoutBinding =
        CoreTestLayoutBinding.inflate(layoutInflater)

    override fun CoreTestLayoutBinding.bind() {}

    @SmallTest
    fun test_ViewModelInjection() {
        Assert.assertNotNull(vm)
        Assert.assertEquals("TestViewModel", vm.string)

        vm {
            Assert.assertEquals("TestViewModel", string)
        }
    }

}