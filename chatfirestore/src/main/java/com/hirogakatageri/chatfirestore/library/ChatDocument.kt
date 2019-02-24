package com.hirogakatageri.chatfirestore.library

/**
 * Document class that holds the channel Id and chats of users.
 * */
data class ChatDocument(val channelId: String) {

    val chats = arrayListOf<String>()

    /**
     * Retrieves chat from any position.
     * */
    inline fun <reified T> getChat(position: Int): T = Chat.valueOf(chats[position])

    /**
     * Retrieves chat from last position.
     * */
    inline fun <reified T> getLastChat(): T = Chat.valueOf(chats.last())

}