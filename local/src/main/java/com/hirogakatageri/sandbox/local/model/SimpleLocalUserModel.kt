package com.hirogakatageri.sandbox.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey
import com.hirogakatageri.sandbox.local.model.base.IUserModel

@Fts4(contentEntity = LocalUserModel::class)
@Entity(tableName = "users_fts")
data class SimpleLocalUserModel(
    @PrimaryKey @ColumnInfo(name = "rowid") override val uid: Int,
    @ColumnInfo(name = "username") override var username: String,
    @ColumnInfo(name = "html_url") override var htmlUrl: String? = "",
    @ColumnInfo(name = "profile_image_url") override var profileImageUrl: String? = null,
    @ColumnInfo(name = "notes") override var notes: String? = null
) : IUserModel