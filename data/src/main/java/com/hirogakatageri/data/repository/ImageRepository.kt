package com.hirogakatageri.data.repository

import com.google.firebase.storage.FirebaseStorage
import com.hirogakatageri.data.OnFailureListener
import com.hirogakatageri.data.OnUploadSuccessListener
import java.io.File
import java.io.FileInputStream

private const val IMAGES_PATH = "images"

class ImageRepository(private val storage: FirebaseStorage) {

    fun uploadImage(file: File, filename: String, onFailure: OnFailureListener, onSuccess: OnUploadSuccessListener) {
        val reference = storage.reference.child("images").child("/$filename")
        val stream = FileInputStream(file)
        reference.putStream(stream)
            .addOnFailureListener(onFailure)
            .addOnSuccessListener(onSuccess)
    }

}