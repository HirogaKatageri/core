package com.hirogakatageri.chatfirestore.library

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException

open class BaseChatManager<C : IChat>(
    override val firestore: FirebaseFirestore,
    override val rootCollectionPath: String,
    override val chatCollectionPath: String,
    override val subCollectionDocumentPath: String
) : IChatManager {

    /**
     * Inserts chat into Firestore.
     * @param onSuccess Returns document reference.
     * @param onFailure Returns exception.
     * */
    fun insertChat(
        chat: C,
        onSuccess: () -> Unit,
        onFailure: (ex: Exception) -> Unit
    ) {
        firestore.collection(rootCollectionPath)
            .document(chatCollectionPath)
            .collection(subCollectionDocumentPath)
            .document(chat.chatId)
            .set(chat)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    /**
     * Removes chat in Firestore.
     * @param chatId Used to find document to delete in sub-collection.
     * @param onSuccess When successfully delete chat.
     * @param onFailure When fails to delete chat.
     * */
    fun removeChat(
        chatId: String,
        onSuccess: () -> Unit,
        onFailure: (ex: Exception) -> Unit
    ) {
        firestore.collection(rootCollectionPath)
            .document(chatCollectionPath)
            .collection(subCollectionDocumentPath)
            .document(chatId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { ex -> onFailure(ex) }
    }

    /**
     * Observes Chat for any new changes.
     * @param onQuery Returns list of chats.
     * @param onError Returns firebaseFirestore Error.
     * */
    inline fun <reified C : IChat> observeChat(
        crossinline onQuery: (list: List<C>) -> Unit,
        crossinline onError: (ex: FirebaseFirestoreException) -> Unit
    ) {
        firestore.collection(rootCollectionPath)
            .document(chatCollectionPath)
            .collection(subCollectionDocumentPath)
            .addSnapshotListener { _query, exception ->
                if (_query != null) onQuery(_query.toObjects(C::class.java))
                else if (exception != null) onError(exception)
            }
    }
}