package com.hirogakatageri.chatfirestore.library

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

abstract class AbstractChatManager<C : AbstractChat> {

    /**
     * Firestore instance. @see Firestore
     * */
    abstract val firestore: FirebaseFirestore

    /**
     * Collection path in firestore where channel documents will be located.
     * */
    open val collectionPath: String = "chats"

    /**
     * Field name of chats in document.
     * */
    open val chatPath: String = "chats"

    /**
     * Timezone used to calculate the time when chat is sent. Default is "GMT +8"
     * */
    open val timeZone: String = "GMT +8"

    var _chat: C? = null
        ?: throw NullPointerException("Chat is not yet initialized, call createChat() to initialize chat.")

    fun createChannel(
        onSuccess: (channelId: String) -> Unit,
        onFailure: (ex: Exception) -> Unit
    ) {
        val channelId = UUID.randomUUID().toString()

        val document = ChatDocument(channelId)

        firestore.collection(collectionPath).document(channelId)
            .set(document)
            .addOnSuccessListener {
                onSuccess(channelId)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    inline fun <reified T> observeChannel(
        channelId: String,
        crossinline onSuccess: (T) -> Unit,
        crossinline onFailure: (ex: Exception) -> Unit
    ) {
        firestore.collection(collectionPath).document(channelId)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot != null)
                    onSuccess(
                        documentSnapshot.toObject(T::class.java)
                            ?: throw IllegalStateException("Unable to parse object.")
                    )
                else if (firebaseFirestoreException != null)
                    onFailure(firebaseFirestoreException)
            }
    }

    fun sendChat(
        channelId: String,
        onSuccess: () -> Unit,
        onFailure: (ex: Exception) -> Unit
    ) {
        firestore.collection(collectionPath).document(channelId)
            .update(chatPath, FieldValue.arrayUnion(_chat?.toJsonString()))
            .addOnSuccessListener {
                onSuccess()
                _chat = null
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun createChat(chat: C): AbstractChatManager<C> {
        _chat = chat
        _chat?.time = Chat.getTimeInMillis(timeZone)
        return this
    }

    fun withMessage(message: String): AbstractChatManager<C> {
        _chat?.message = message
        return this
    }
}