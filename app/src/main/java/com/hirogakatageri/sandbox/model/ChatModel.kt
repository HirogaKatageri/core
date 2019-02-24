package com.hirogakatageri.sandbox.model

import com.hirogakatageri.sandbox.base.AbstractChat


class ChatModel(
    override var timeZone: String = "GMT +8",
    override var userId: String = "",
    override var message: String = ""
) : AbstractChat