package dev.hirogakatageri.sandbox.ui.chat

import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.databinding.FragmentChatBinding
import dev.hirogakatageri.sandbox.util.enableMultiLineDoneAction
import org.koin.androidx.viewmodel.ext.android.viewModel

class FireChatFragment : CoreViewModelFragment<FragmentChatBinding, FireChatViewModel>() {

    override val vm: FireChatViewModel by viewModel()

    override fun createBinding(container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(layoutInflater, container, false)

    override fun FragmentChatBinding.bind() {
        txtMessageInput.enableMultiLineDoneAction()
        txtMessageInput.setOnEditorActionListener { view, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val message = view.text.toString()
                view.text = null

                if (message.isNotBlank()) vm.sendMessage(message)
            }

            true
        }
    }

}