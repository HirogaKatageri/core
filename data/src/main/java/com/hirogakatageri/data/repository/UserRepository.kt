package com.hirogakatageri.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.internal.api.FirebaseNoSignedInUserException
import com.hirogakatageri.data.OnAddSuccessListener
import com.hirogakatageri.data.OnFailureListener
import com.hirogakatageri.data.OnQuerySuccessListener
import com.hirogakatageri.data.firestore.model.UserStoreModel

private const val ROOT = "users"

class UserRepository(private val auth: FirebaseAuth, private val firestore: FirebaseFirestore) {

    fun getUser(id: String, onFailure: OnFailureListener, onSuccess: OnQuerySuccessListener) {
        firestore.collection(ROOT)
            .whereEqualTo("id", id)
            .get()
            .addOnFailureListener(onFailure)
            .addOnSuccessListener(onSuccess)
    }

    fun createUser(onFailure: OnFailureListener, onSuccess: OnAddSuccessListener) {
        val user = auth.currentUser

        if (user == null) onFailure(FirebaseNoSignedInUserException("User is not signed in."))
        else {
            val username = user.displayName
            val email = user.email

            if (username != null && email != null) {
                val model = UserStoreModel(user.uid, username, email)

                firestore.collection(ROOT)
                    .add(model)
                    .addOnFailureListener(onFailure)
                    .addOnSuccessListener(onSuccess)
            } else onFailure(IllegalArgumentException("Username or Email is null"))
        }
    }

}