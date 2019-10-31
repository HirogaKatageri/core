package com.hirogakatageri.data.repository

import android.app.Activity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.hirogakatageri.data.OnAddSuccessListener
import com.hirogakatageri.data.OnFailureListener
import com.hirogakatageri.data.OnQuerySuccessListener
import com.hirogakatageri.data.OnUpdateSuccessListener
import com.hirogakatageri.data.base.BlogPost

private const val ROOT = "posts"

class BlogRepository(private val firestore: FirebaseFirestore) {

    fun getPosts(
        listener: (snapshot: QuerySnapshot?, ex: FirebaseFirestoreException?) -> Unit
    ) = firestore.collection(ROOT)
        .whereEqualTo("private", false)
        .addSnapshotListener(listener)

    fun getPostsByUsername(
        activity: Activity,
        username: String,
        onFailure: OnFailureListener,
        onSuccess: OnQuerySuccessListener
    ) {
        firestore.collection(ROOT)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .whereEqualTo("username", username)
            .get()
            .addOnFailureListener(activity, onFailure)
            .addOnSuccessListener(activity, onSuccess)
    }

    fun updatePostPrivacy(
        activity: Activity,
        isPrivate: Boolean,
        path: String,
        onFailure: OnFailureListener,
        onSuccess: OnUpdateSuccessListener
    ) {
        firestore.collection(ROOT)
            .document(path)
            .update("private", isPrivate)
            .addOnFailureListener(activity, onFailure)
            .addOnSuccessListener(activity, onSuccess)
    }

    fun createBlogPost(
        post: BlogPost,
        onFailure: OnFailureListener,
        onSuccess: OnAddSuccessListener
    ) {
        firestore.collection(ROOT)
            .add(post)
            .addOnFailureListener(onFailure)
            .addOnSuccessListener(onSuccess)
    }

}