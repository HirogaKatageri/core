package com.hirogakatageri.chatfirestore.library

interface ChatResult<T> {

    fun onSuccess(result: T?)

    fun onFailure(exception: Exception)

}