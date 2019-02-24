package com.hirogakatageri.chatfirestore.library

import com.google.gson.Gson
import java.util.*

/**
 * Base chat interface stores necessary details in a chat message.
 * */
abstract class AbstractChat(
    /**
     * Unique Id of user.
     * */
    val userId: String
) {

    /**
     * Unique Id of chat.
     * */
    val chatId: String = UUID.randomUUID().toString()

    /**
     * Time chat was sent. Default value will be calculated with a timezone of GMT+8.
     * */
    var time: Long = 0L

    /**
     * Message sent by user.
     * */
    var message: String = ""

    /**
     * Use this method to compile your data into a String to add into your Firestore Array
     * */
    fun toJsonString(): String = Gson().toJson(this)

}