package dev.hirogakatageri.sandbox.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseManager {

    val auth: FirebaseAuth = Firebase.auth
    val currentUser get() = auth.currentUser

}