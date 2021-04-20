package com.hirogakatageri.core.sample.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.withContext
import org.threeten.bp.LocalDateTime

@ObsoleteCoroutinesApi
class Clock {

    private lateinit var ticker: ReceiveChannel<Unit>

    val time: String get() = LocalDateTime.now().toString()

    suspend fun start(onTick: (time: String) -> Unit) = withContext(Dispatchers.IO) {
        ticker = ticker(
            delayMillis = 1000,
            context = Dispatchers.IO
        )

        for (event in ticker) onTick(time)
    }

    suspend fun stop() = withContext(Dispatchers.IO) {
        ticker.cancel()
    }

}