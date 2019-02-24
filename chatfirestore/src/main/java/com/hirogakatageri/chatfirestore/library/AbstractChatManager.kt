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


    /**
     * Generic Chat variable, must inherit Abstract Chat.
     * */
    var _chat: C? = null
        ?: throw NullPointerException("Chat is not yet initialized, call createChat() to initialize chat.")


    /**
     * Creates channel collection in firestore.
     * @param onSuccess Returns channel Id for your use.
     * @param onFailure Returns exception for your use.
     * */
    fun createChannel(
        onSuccess: (channelId: String) -> Unit,
        onFailure: (ex: Exception) -> Unit
    ) {
        val channelId = UUID.randomUUID().toString()

        val document = AbstractChatDocument(channelId)

        firestore.collection(collectionPath).document(channelId)
            .set(document)
            .addOnSuccessListener {
                onSuccess(channelId)
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Observes channel for any new chats.
     * @param channelId The channel Id that leads to the channel you'll be observing.
     * @param onSuccess Returns your generic object for your use, it should contain the data you need right?
     * @param onFailure Returns an exception for your use, also tells you it failed.
     * */
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

    /**
     * Sends a chat in the channel, preferably use this after creating a chat first.
     * @see createChat
     * @param channelId The channel Id.
     * @param onSuccess Tells you if it succeeds.
     * @param onFailure Returns an exception and also tells you if it fails.
     * */
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

    /**
     * Initializes a chat message, automatically inserts time as well, depending on your timezone. Default is GMT+8
     * @param chat The chat object. It's public... Good if you want to implement your own Chat Manager right?
     * */
    fun createChat(chat: C): AbstractChatManager<C> {
        _chat = chat
        _chat?.time = Chat.getTimeInMillis(timeZone)
        return this
    }

    /**
     * The function that inserts your message into the chat, builder style.
     * @param message It contains your message.
     * */
    fun withMessage(message: String): AbstractChatManager<C> {
        _chat?.message = message
        return this
    }
}