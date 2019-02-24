package com.hirogakatageri.sandbox.manager

import com.google.firebase.firestore.FirebaseFirestore
import com.hirogakatageri.sandbox.base.AbstractChatManager


class ChatManager(
    override val firestore: FirebaseFirestore,
    override val collectionPath: String,
    override val chatPath: String
) : AbstractChatManager