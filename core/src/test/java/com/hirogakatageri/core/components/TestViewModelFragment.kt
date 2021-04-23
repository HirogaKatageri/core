package com.hirogakatageri.core.components

import android.view.ViewGroup
import androidx.test.filters.SmallTest
import com.hirogakatageri.core.databinding.CoreFragmentTestLayoutBinding
import com.hirogakatageri.core.fragment.CoreViewModelFragment
import org.junit.Assert
import org.koin.androidx.viewmodel.ext.android.viewModel

class TestViewModelFragment :
    CoreViewModelFragment<CoreFragmentTestLayoutBinding, TestViewModel>() {

    override val vm: TestViewModel by viewModel()

    override fun createBinding(container: ViewGroup?): CoreFragmentTestLayoutBinding =
        CoreFragmentTestLayoutBinding.inflate(layoutInflater, container, false)

    override fun CoreFragmentTestLayoutBinding.bind() {}

    @SmallTest
    fun test_ViewModelInjection() {
        Assert.assertNotNull(vm)
        Assert.assertEquals("TestViewModel", vm.string)

        vm {
            Assert.assertEquals("TestViewModel", string)
        }
    }
}