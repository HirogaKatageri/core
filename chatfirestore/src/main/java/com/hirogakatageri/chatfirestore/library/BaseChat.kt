package com.hirogakatageri.chatfirestore.library

import java.util.*

open class BaseChat(
    override val chatId: String = UUID.randomUUID().toString(),
    override val userId: String = UUID.randomUUID().toString(),
    override val message: String = ""
) : IChat