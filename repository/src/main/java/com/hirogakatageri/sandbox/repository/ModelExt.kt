package com.hirogakatageri.sandbox.repository

import com.hirogakatageri.sandbox.local.model.LocalUserModel
import com.hirogakatageri.sandbox.remote.model.RemoteUserModel

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