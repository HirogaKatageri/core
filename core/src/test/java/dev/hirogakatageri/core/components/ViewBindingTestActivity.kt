package dev.hirogakatageri.core.components

import dev.hirogakatageri.core.activity.CoreViewBindingActivity
import dev.hirogakatageri.core.databinding.CoreTestLayoutBinding

class ViewBindingTestActivity : CoreViewBindingActivity<CoreTestLayoutBinding>() {

    override fun createBinding(): CoreTestLayoutBinding =
        CoreTestLayoutBinding.inflate(layoutInflater)

    override fun CoreTestLayoutBinding.bind() {
        textView.text = "Alpha"
    }
}
