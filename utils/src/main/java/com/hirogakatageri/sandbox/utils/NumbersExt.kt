package com.hirogakatageri.sandbox.utils

import androidx.annotation.Keep
import java.util.*

@Keep
fun Long.abbreviate(): String {

    var value = this

    val map = TreeMap<Long, String>()
    map[1_000L] = "k"
    map[1_000_000L] = "m"
    map[1_000_000_000L] = "g"
    map[1_000_000_000_000L] = "t"
    map[1_000_000_000_000_000L] = "p"
    map[1_000_000_000_000_000_000L] = "e"

    if (this == Long.MIN_VALUE) value = Long.MIN_VALUE + 1
    if (value < 0) return "-" + value.abbreviate()
    if (value < 1000) return this.toString()

    val (divideBy, suffix) = map.floorEntry(value)

    val truncated = value / (divideBy / 10) //the number part of the output times 10
    val hasDecimal = truncated < 100 && truncated / 10.0 != (truncated / 10).toDouble()
    return if (hasDecimal) "${truncated / 10.0}$suffix" else "${truncated / 10}$suffix"
}