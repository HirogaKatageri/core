package com.hirogakatageri.repository

import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.remote.model.RemoteUserModel

fun RemoteUserModel.toLocalUserModel(): LocalUserModel = LocalUserModel(
        uid = id,
        username = login,
        followers = followers ?: 0,
        followings = following ?: 0,
        profileImageUrl = avatarUrl,
        name = name,
        companyName = company,
        blogUrl = blog,
        htmlUrl = htmlUrl
)