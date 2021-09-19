package dev.hirogakatageri.sandbox.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import dev.hirogakatageri.sandbox.data.chat.FireChatRepository
import dev.hirogakatageri.sandbox.data.chat.model.ChatMessageModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class FireChatViewModel(
    val chatRepository: FireChatRepository
) : ViewModel() {

    private val _messages: MutableStateFlow<List<ChatMessageModel>> = MutableStateFlow(emptyList())
    val messages: StateFlow<List<ChatMessageModel>> = _messages

    override fun onCleared() {
        super.onCleared()
        chatRepository.detachConversationListener()
    }

    fun sendMessage(message: String) = viewModelScope.launch {
        chatRepository.sendMessage(message)
    }

    fun listenToConversation() = viewModelScope.launch {
        chatRepository.listenToConversation(::onFirstMessages, ::onNewMessage)
    }

    private fun onFirstMessages(task: Task<QuerySnapshot>) = viewModelScope.launch {
        val snapshots = task.await()
        val newMessages = snapshots.toObjects(ChatMessageModel::class.java)

        _messages.value = newMessages.reversed()
    }

    private fun onNewMessage(doc: QueryDocumentSnapshot) = viewModelScope.launch {
        val message = doc.toObject(ChatMessageModel::class.java)
        val newList = mutableListOf<ChatMessageModel>()
        newList.addAll(messages.value)
        newList.add(message)

        _messages.value = newList
    }

}