package com.hirogakatageri.remote.call

import okhttp3.Headers

object RemoteHelpers {

    fun getNextPageOfUsers(headers: Headers): Int {
        val link = headers.get("link") // Header of the next page
        val pagePattern = "(\\?since=\\d+)" // Pattern to find the next page
        val result = Regex(pagePattern).find(link ?: "")
        val value = result?.value?.removePrefix("since=")
        return value?.toInt() ?: 0
    }
}