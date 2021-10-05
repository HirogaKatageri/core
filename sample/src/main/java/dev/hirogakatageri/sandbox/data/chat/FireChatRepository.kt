package dev.hirogakatageri.sandbox.data.chat

import com.github.ajalt.timberkt.e
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import dev.hirogakatageri.sandbox.data.FirebaseManager
import dev.hirogakatageri.sandbox.data.chat.model.ChatMessageModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FireChatRepository(
    private val firebase: FirebaseManager
) {

    private var isFirstQuery: Boolean = true
    private var conversationListener: ListenerRegistration? = null

    fun detachConversationListener() {
        conversationListener?.remove()
    }

    suspend fun sendMessage(
        message: String
    ) = withContext(Dispatchers.IO) {
        val model = ChatMessageModel(
            message = message,
            email = firebase.currentUser?.email ?: "Anonymous Onion"
        )

        firebase.chats.add(model)
    }

    suspend fun listenToConversation(
        onFirstQuery: (task: Task<QuerySnapshot>) -> Unit,
        onNewSnapshot: (document: QueryDocumentSnapshot) -> Unit
    ) = withContext(Dispatchers.IO) {
        conversationListener = firebase.chats.addSnapshotListener { snapshot, error ->
            if (error != null) {
                e(error)
                return@addSnapshotListener
            }

            val isSourceLocal = snapshot != null && snapshot.metadata.hasPendingWrites()

            if (snapshot != null && !isSourceLocal) {

                if (isFirstQuery) {
                    // Get initial items
                    val messages = snapshot.query
                        .orderBy("timeSent", Query.Direction.DESCENDING)
                        .limit(10)
                        .get()

                    isFirstQuery = false
                    onFirstQuery(messages)
                } else {
                    // Get changes
                    val message = snapshot.documentChanges
                        .first()
                        .document

                    onNewSnapshot(message)
                }
            }
        }
    }
}
