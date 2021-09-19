package dev.hirogakatageri.sandbox.data.chat.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.ServerTimestamp

data class ChatMessageModel(
    @DocumentId val id: String? = null,
    val email: String? = null,
    val message: String? = null,
    @ServerTimestamp val timeSent: Timestamp? = null
)
