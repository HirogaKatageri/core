package com.hirogakatageri.sandbox.manager

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.hirogakatageri.sandbox.document.ChatDocument
import com.hirogakatageri.sandbox.model.ChatModel
import java.util.*


/**
 *  Created by Gian Patrick Quintana on 2/21/19.
 *  Copyright 2019 Nexplay. All rights reserved.
 */
class ChatManager(val firestore: FirebaseFirestore) {

    private val collectionPath = "chats"
    private val arrayPath = "chats"

    fun createChannel(onSuccess: () -> Unit, onFailure: (exception: Exception) -> Unit) {

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

    fun observeChat(
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

    fun addChat(
        channelId: String,
        userId: String,
        msg: String,
        onSuccess: () -> Unit,
        onFailure: (exception: Exception) -> Unit
    ) {

        val chat = ChatModel(userId = userId, msg = msg)

        firestore.collection(collectionPath).document(channelId)
            .update(arrayPath, FieldValue.arrayUnion(chat.toString()))
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

}