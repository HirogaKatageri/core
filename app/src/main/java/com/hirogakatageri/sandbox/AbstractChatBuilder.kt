package com.hirogakatageri.sandbox

import com.hirogakatageri.sandbox.base.AbstractChat

abstract class AbstractChatBuilder<T : AbstractChat> {

    private inline lateinit var _chat: T

    inline fun <reified T>create() {
        _chat
    }

}