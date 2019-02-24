package com.hirogakatageri.sandbox.document

import com.hirogakatageri.sandbox.model.ChatModel



data class ChatDocument(val channelId: String) {

    val chats = arrayListOf<String>()

    fun getChat(position: Int): ChatModel = ChatModel.valueOf(chats[position])
}