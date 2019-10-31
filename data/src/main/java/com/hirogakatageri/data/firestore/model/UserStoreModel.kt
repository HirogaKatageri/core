package com.hirogakatageri.data.firestore.model

import com.hirogakatageri.data.base.User

data class UserStoreModel(
    override val id: String,
    override val username: String,
    override val email: String
) : User