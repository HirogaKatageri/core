package com.hirogakatageri.chatfirestore.library

interface IChat {

    /**
     * Unique chat ID
     * */
    val chatId: String

    /**
     * Unique user ID
     * */
    val userId: String

    /**
     * Where chat is stored.
     * */
    val message: String
}