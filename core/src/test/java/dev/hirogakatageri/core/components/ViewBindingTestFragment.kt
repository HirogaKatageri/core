package dev.hirogakatageri.core.components

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import dev.hirogakatageri.core.databinding.CoreFragmentTestLayoutBinding
import dev.hirogakatageri.core.fragment.CoreViewBindingFragment

class ViewBindingTestFragment : CoreViewBindingFragment<CoreFragmentTestLayoutBinding>() {

    override fun createBinding(container: ViewGroup?): CoreFragmentTestLayoutBinding =
        CoreFragmentTestLayoutBinding.inflate(layoutInflater, container, false)

    override fun CoreFragmentTestLayoutBinding.bind() {
        fragmentTextView.text = "Alpha"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show()
    }
}
