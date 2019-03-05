package com.hirogakatageri.chatfirestore.library

import com.google.firebase.firestore.FirebaseFirestore

interface IChatManager {

    /**
     *  The Firestore.
     * */
    val firestore: FirebaseFirestore

    /**
     * The first collection that contains the channel document of chats.
     * */
    val rootCollectionPath: String

    /**
     * The channel that contains the sub-collection of chats.
     * */
    val subCollectionDocumentPath: String

    /**
     * The sub-collection that contains the chats.
     * */
    val chatCollectionPath: String
}