package com.hirogakatageri.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hirogakatageri.local.dao.UserDao
import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.local.model.SimpleLocalUserModel

@Database(
    entities = arrayOf(
        LocalUserModel::class,
        SimpleLocalUserModel::class
    ), version = 1
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}