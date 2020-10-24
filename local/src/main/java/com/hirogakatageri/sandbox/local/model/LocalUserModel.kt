package com.hirogakatageri.sandbox.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hirogakatageri.sandbox.local.model.base.IUserModel

@Entity(tableName = "users")
data class LocalUserModel(
    @PrimaryKey @ColumnInfo(name = "rowid") override val uid: Int,
    @ColumnInfo(name = "username") override var username: String,
    @ColumnInfo(name = "followers") var followers: Int = 0,
    @ColumnInfo(name = "followings") var followings: Int = 0,
    @ColumnInfo(name = "profile_image_url") override var profileImageUrl: String? = null,
    @ColumnInfo(name = "name") var name: String? = null,
    @ColumnInfo(name = "company_name") var companyName: String? = null,
    @ColumnInfo(name = "blog_url") var blogUrl: String? = null,
    @ColumnInfo(name = "notes") override var notes: String? = null,
    @ColumnInfo(name = "html_url") override var htmlUrl: String? = null
) : IUserModel