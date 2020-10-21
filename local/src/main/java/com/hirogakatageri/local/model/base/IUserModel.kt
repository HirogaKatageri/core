package com.hirogakatageri.local.model.base

interface IUserModel {
    val uid: Int
    var username: String
    var htmlUrl: String?
    var profileImageUrl: String?
    var notes: String?
}