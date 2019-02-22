package com.hirogakatageri.sandbox.model

import java.util.*


/**
 *  Created by Gian Patrick Quintana on 2/21/19.
 *  Copyright 2019 Nexplay. All rights reserved.
 */
data class ChatModel(
    val time: Long = Calendar.getInstance(TimeZone.getTimeZone("GMT + 8")).timeInMillis,
    val userId: String = "",
    val msg: String = ""
) {

    companion object {
        fun valueOf(string: String): ChatModel {
            if (string.contains(";")) {
                val item = string.split(";")

                return ChatModel(item[0].toLong(), item[1], item[2])

            } else throw IllegalStateException("Wrong delimiter.")
        }
    }

    override fun toString(): String = "$time;$userId;$msg"
}