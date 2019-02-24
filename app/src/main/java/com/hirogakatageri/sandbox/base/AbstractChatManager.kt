package com.hirogakatageri.sandbox.base

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hirogakatageri.sandbox.document.ChatDocument
import java.util.*
import kotlin.reflect.KClass

/**
 * Base interface of Chat Manager contains necessary variables and functions for handling chats in Firebase Firestore.
 * */
interface AbstractChatManager {

    /**
     * Instance of Firebase Firestore.
     * @see FirebaseFirestore
     * */
    val firestore: FirebaseFirestore

    /**
     * Id of Collection in Firestore.
     * */
    val collectionPath: String

    /**
     * Field name of chat array in document of Firestore.
     * */
    val chatPath: String

    /**
     * Creates a Channel for the Chat.
     * */
    fun createChannel(
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        val channelId = UUID.randomUUID().toString()

        val document = ChatDocument(channelId)

        firestore.collection(collectionPath).document(channelId)
            .set(document)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /**
     * Observes chat for any changes.
     * */
    fun <T : AbstractChat> observeChat(
        channelId: String,
        onSuccess: (document: DocumentSnapshot) -> Unit,
        onFailure: (exception: FirebaseFirestoreException) -> Unit
    ) {
        firestore.collection(collectionPath).document(channelId)
            .addSnapshotListener { documentSnapshot, firebaseFirestoreException ->
                if (documentSnapshot != null)
                    onSuccess(documentSnapshot)
                else if (firebaseFirestoreException != null)
                    onFailure(firebaseFirestoreException)
            }
    }

    /**
     * Adds chat in Channel.
     * */
    fun <T : AbstractChat> addChat(
        channelId: String,
        chat: T,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {
        firestore.collection(collectionPath).document(channelId)
            .update(chatPath, FieldValue.arrayUnion(chat))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}