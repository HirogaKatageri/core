package com.hirogakatageri.blog.list

import android.app.Activity
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.hirogakatageri.data.OnAddSuccessListener
import com.hirogakatageri.data.OnFailureListener
import com.hirogakatageri.data.OnUploadSuccessListener
import com.hirogakatageri.data.base.BlogPost
import com.hirogakatageri.data.repository.BlogRepository
import com.hirogakatageri.data.repository.ImageRepository
import java.io.File
import java.util.*

class BlogPostListRepository(
    private val blogRepo: BlogRepository,
    private val imageRepo: ImageRepository
) {

    fun getPosts(
        listener: (snapshot: QuerySnapshot?, ex: FirebaseFirestoreException?) -> Unit
    ) = blogRepo.getPosts( listener)

    fun uploadImage(file: File, onFailure: OnFailureListener, onSuccess: OnUploadSuccessListener) {
        imageRepo.uploadImage(file, UUID.randomUUID().toString(), onFailure, onSuccess)
    }

    fun createPost(
        blogPost: BlogPost,
        onFailure: OnFailureListener,
        onSuccess: OnAddSuccessListener
    ) = blogRepo.createBlogPost(blogPost, onFailure, onSuccess)

}