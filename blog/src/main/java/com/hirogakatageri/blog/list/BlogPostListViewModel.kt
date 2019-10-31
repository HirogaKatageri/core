package com.hirogakatageri.blog.list

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.hirogakatageri.data.firestore.model.BlogPostStoreModel
import java.io.File
import java.util.*

class BlogPostListViewModel(private val repository: BlogPostListRepository, private val auth: FirebaseAuth) : ViewModel() {

    val blogPosts = MutableLiveData<List<BlogPostStoreModel>>()
    val isUploadSuccessful = MutableLiveData<Boolean>()
    val isPostSuccessful = MutableLiveData<Boolean>()

    var imageUri: Uri? = null
    var postsListener: ListenerRegistration? = null

    fun getPosts() {
        postsListener = repository.getPosts { snapshot, ex ->
            snapshot?.run {
                if (!metadata.hasPendingWrites()) {
                    val list = toObjects(BlogPostStoreModel::class.java)
                    blogPosts.value = list
                }
            }
        }
    }

    fun uploadImage(file: File) {
        repository.uploadImage(
            file,
            onFailure = { isUploadSuccessful.value = false },
            onSuccess = { snapshot ->
                isUploadSuccessful.value = true
                snapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri -> imageUri = uri }
            }
        )
    }

    fun submitPost(message: String) {

        val userId = auth.currentUser?.uid

        if (userId == null) {
            isPostSuccessful.value = false
        } else {
            val blogPost = BlogPostStoreModel(
                UUID.randomUUID().toString(),
                userId,
                message,
                imageUri?.toString()
            )

            repository.createPost(
                blogPost,
                onFailure = { isPostSuccessful.value = false },
                onSuccess = { isPostSuccessful.value = true }
            )
        }
    }

    override fun onCleared() {
        postsListener?.remove()
        super.onCleared()
    }
}