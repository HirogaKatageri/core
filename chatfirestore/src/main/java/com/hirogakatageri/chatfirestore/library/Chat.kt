package com.hirogakatageri.chatfirestore.library

import com.google.gson.Gson
import java.util.*

/**
 * Chat util class
 * */
class Chat {

    companion object {

        /**
         * Returns object from json string.
         * */
        inline fun <reified T> valueOf(jsonString: String): T =
            Gson().fromJson(jsonString, T::class.java)

        /**
         * Gets current time in Millis.
         * */
        fun getTimeInMillis(timeZone: String) =
            Calendar.getInstance(TimeZone.getTimeZone(timeZone)).timeInMillis
    }

}