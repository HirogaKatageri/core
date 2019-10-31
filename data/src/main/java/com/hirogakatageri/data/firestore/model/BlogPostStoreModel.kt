package com.hirogakatageri.data.firestore.model

import com.google.firebase.firestore.ServerTimestamp
import com.hirogakatageri.data.base.BlogPost
import java.util.*

data class BlogPostStoreModel(
    override val id: String = "",
    override val userId: String = "",
    override var message: String = "",
    override var imageUrl: String? = null,
    @ServerTimestamp override var timestamp: Date? = null,
    override var private: Boolean = false
) : BlogPost