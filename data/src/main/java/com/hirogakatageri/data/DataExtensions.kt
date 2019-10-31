package com.hirogakatageri.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.UploadTask

typealias OnFailureListener = (ex: Exception) -> Unit
typealias OnQuerySuccessListener = (snapshot: QuerySnapshot?) -> Unit
typealias OnUpdateSuccessListener = (_: Void) -> Unit
typealias OnAddSuccessListener = (reference : DocumentReference?) -> Unit
typealias OnUploadSuccessListener = (snapshot: UploadTask.TaskSnapshot) -> Unit
