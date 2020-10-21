package com.hirogakatageri.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hirogakatageri.local.model.base.IUserModel

@Entity(tableName = "users")
data class LocalUserModel(
    @PrimaryKey @ColumnInfo(name = "rowid") override val uid: Int,
    override var username: String,
    var followers: Int = 0,
    var followings: Int = 0,
    @ColumnInfo(name = "profile_image_url") override var profileImageUrl: String? = null,
    var name: String? = null,
    @ColumnInfo(name = "company_name") var companyName: String? = null,
    var blogUrl: String? = null,
    override var notes: String? = null,
    override var htmlUrl: String? = null
) : IUserModel