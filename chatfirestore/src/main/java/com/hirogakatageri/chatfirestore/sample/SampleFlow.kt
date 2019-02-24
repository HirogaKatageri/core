package com.hirogakatageri.chatfirestore.sample

import com.google.firebase.firestore.FirebaseFirestore

class SampleFlow {

    fun start() {
        val manager = SampleChatManager(FirebaseFirestore.getInstance())

        manager.createChat(SampleChat("00001"))
            .withMessage("Test Sample")
            .sendChat("Channel Id",
                onSuccess = { /*Do something here.*/ },
                onFailure = { /*Do something here.*/ })
    }

}