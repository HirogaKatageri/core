package com.hirogakatageri.remote.call

import okhttp3.Headers

object RemoteHelpers {

    fun getNextPageOfUsers(headers: Headers): Long? {
        val link = headers.get("link") // Header of the next page
        val pattern = "(\\?since=\\d+)" // Pattern to find the next page
        val result = Regex(pattern).find(link ?: "")
        val values = result?.value?.split('=')
        return values?.get(1)?.toLong() ?: 0
    }

}