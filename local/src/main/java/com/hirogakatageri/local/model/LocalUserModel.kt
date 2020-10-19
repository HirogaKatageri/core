package com.hirogakatageri.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUserModel(
    @PrimaryKey @ColumnInfo(name = "rowid") val uid: Long,
    var username: String,
    var followers: Int = 0,
    var followings: Int = 0,
    @ColumnInfo(name = "profile_image_url") var profileImageUrl: String? = null,
    var name: String? = null,
    @ColumnInfo(name = "company_name") var companyName: String? = null,
    var blogUrl: String? = null,
    var notes: String? = null
)