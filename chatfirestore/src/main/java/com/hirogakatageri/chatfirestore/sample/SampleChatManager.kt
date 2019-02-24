package com.hirogakatageri.chatfirestore.sample

import com.google.firebase.firestore.FirebaseFirestore
import com.hirogakatageri.chatfirestore.library.AbstractChatManager

class SampleChatManager(
    override val firestore: FirebaseFirestore
) : AbstractChatManager<SampleChat>() {

    fun withSample(sample: String): SampleChatManager {
        _chat?.sampleVar = sample
        return this
    }

}