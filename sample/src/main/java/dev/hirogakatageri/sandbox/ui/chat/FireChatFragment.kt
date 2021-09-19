package dev.hirogakatageri.sandbox.ui.chat

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.RecyclerView
import dev.hirogakatageri.core.fragment.CoreViewModelFragment
import dev.hirogakatageri.sandbox.databinding.FragmentChatBinding
import dev.hirogakatageri.sandbox.util.enableMultiLineDoneAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FireChatFragment : CoreViewModelFragment<FragmentChatBinding, FireChatViewModel>() {

    private val controller: FireChatMsgController by inject()
    private val chatObserver by lazy { ChatObserver() }

    override val vm: FireChatViewModel by viewModel()

    override fun createBinding(container: ViewGroup?): FragmentChatBinding =
        FragmentChatBinding.inflate(layoutInflater, container, false)

    override fun FragmentChatBinding.bind() {
        setupMessageInput()
        setupMessages()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch { observeMessages() }
            launch { vm.listenToConversation() }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.recyclerView?.adapter?.unregisterAdapterDataObserver(chatObserver)
    }

    private fun setupMessageInput() = binding {
        txtMessageInput.enableMultiLineDoneAction()
        txtMessageInput.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                val message = view.text.toString()
                view.text = null

                if (message.isNotBlank()) vm.sendMessage(message)
            }

            true
        }
    }

    private fun setupMessages() = binding {
        recyclerView.setController(controller)
        recyclerView.adapter?.registerAdapterDataObserver(chatObserver)
    }

    private suspend fun observeMessages() = withContext(Dispatchers.Main) {
        vm.messages.collect { list ->
            controller.messages = list
            controller.requestModelBuild()
        }
    }

    private inner class ChatObserver : RecyclerView.AdapterDataObserver() {

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            super.onItemRangeInserted(positionStart, itemCount)
            binding?.recyclerView?.smoothScrollToPosition(controller.adapter.itemCount)
        }

    }

}