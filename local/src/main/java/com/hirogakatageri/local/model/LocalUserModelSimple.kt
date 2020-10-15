package com.hirogakatageri.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4(contentEntity = LocalUserModel::class)
@Entity(tableName = "users_fts")
data class LocalUserModelSimple(
    @PrimaryKey @ColumnInfo(name = "rowid") val uid: Int,
    var username: String,
    var followers: Int? = 0,
    @ColumnInfo(name = "profile_image_url") var profileImageUrl: String? = null,
    var notes: String? = null
)