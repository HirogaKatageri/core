package com.hirogakatageri.sandbox.document

import com.hirogakatageri.sandbox.model.ChatModel


/**
 *  Created by Gian Patrick Quintana on 2/21/19.
 *  Copyright 2019 Nexplay. All rights reserved.
 */
data class ChatDocument(val channelId: String) {

    val chats = arrayListOf<String>()

    fun getChat(position: Int): ChatModel = ChatModel.valueOf(chats[position])
}