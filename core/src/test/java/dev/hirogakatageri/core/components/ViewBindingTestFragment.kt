package dev.hirogakatageri.core.components

import android.view.ViewGroup
import dev.hirogakatageri.core.databinding.CoreFragmentTestLayoutBinding
import dev.hirogakatageri.core.fragment.CoreViewBindingFragment

class ViewBindingTestFragment : CoreViewBindingFragment<CoreFragmentTestLayoutBinding>() {

    override fun createBinding(container: ViewGroup?): CoreFragmentTestLayoutBinding =
        CoreFragmentTestLayoutBinding.inflate(layoutInflater, container, false)

    override fun CoreFragmentTestLayoutBinding.bind() {
        fragmentTextView.text = "Alpha"
    }
}
