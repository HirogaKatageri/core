package dev.hirogakatageri.sandbox.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseManager {

    val db: FirebaseFirestore = Firebase.firestore
    val auth: FirebaseAuth = Firebase.auth
    val currentUser get() = auth.currentUser

    val chats
        get() = db.collection("conversations")
            .document("convo_0")
            .collection("messages")
}
