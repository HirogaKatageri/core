package com.hirogakatageri.data.base

import java.util.*

interface BlogPost {
    val id: String
    val userId: String
    var message: String
    var imageUrl: String?
    var timestamp: Date?
    var private: Boolean
}