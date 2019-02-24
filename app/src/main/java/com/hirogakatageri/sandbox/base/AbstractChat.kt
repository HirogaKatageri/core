package com.hirogakatageri.sandbox.base

import com.google.gson.Gson
import java.util.*


/**
 * Base chat interface stores necessary details in a chat message.
 * */
interface AbstractChat {

    /**
     * Timezone used for calculating current time.
     * @see TimeZone
     * */
    var timeZone: String

    /**
     * Unique Id of user.
     * */
    var userId: String

    /**
     * Message sent by user.
     * */
    var message: String

    /**
     * Returns current time depending on timeZone in Millis.
     * */
    fun getTime() = Calendar.getInstance(TimeZone.getTimeZone(timeZone)).timeInMillis

    /**
     * Use this method to compile your data into a String to add into your Firestore Array
     * */
    fun toJsonString(): String = Gson().toJson(this)

}