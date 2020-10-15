package com.hirogakatageri.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUserModel(
    @PrimaryKey val uid: Int,
    @ColumnInfo val username: String,
    @ColumnInfo val followers: Long,

)