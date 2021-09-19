package dev.hirogakatageri.sandbox.data.chat.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp

data class ChatMessageModel(
    val email: String? = null,
    val message: String? = null,
    @ServerTimestamp val timeSent: Timestamp? = null
)
