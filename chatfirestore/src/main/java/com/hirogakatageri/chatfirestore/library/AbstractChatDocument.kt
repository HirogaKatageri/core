package com.hirogakatageri.chatfirestore.library

/**
 * Document class that holds the channel Id and chats of users. It's open if you want to inherit it in your own class and change it a bit.
 * */
open class AbstractChatDocument(val channelId: String) {

    /**
     * Contains all of the chats.
     * */
    val chats = listOf<String>()

    /**
     * Retrieves chat from any position.
     * */
    inline fun <reified T : AbstractChat> getChat(position: Int): T = Chat.valueOf(chats[position])

    /**
     * Retrieves chat from last position.
     * */
    inline fun <reified T : AbstractChat> getLastChat(): T = Chat.valueOf(chats.last())

    /**
     * Retrieves list of users that have sent message in the channel.
     * */
    inline fun <reified T : AbstractChat> getListOfUserIds() =
        chats.map { item -> (Chat.valueOf<T>(item).userId) }

    /**
     * Retrieves list of message sent in the channel.
     * */
    inline fun <reified T : AbstractChat> getListOfMessages() =
        chats.map { item -> (Chat.valueOf<T>(item).message) }

    /**
     * Gets first chat time in millis.
     * */
    inline fun <reified T : AbstractChat> getFirstMessageTime(): Long =
        chats.map { item -> Chat.valueOf<T>(item) }
            .sortedBy { item -> item.time }
            .first().time


    /**
     * Gets last chat time in millis.
     * */
    inline fun <reified T : AbstractChat> getLastMessagTime(): Long =
        chats.map { item -> Chat.valueOf<T>(item) }
            .sortedBy { item -> item.time }
            .last().time

    /**
     * Gets chat Id for chat, could be useful for nested chats.
     * */
    inline fun <reified T : AbstractChat> getChatId(position: Int) =
        chats.map { item -> (Chat.valueOf<T>(item)) }[position].chatId
}