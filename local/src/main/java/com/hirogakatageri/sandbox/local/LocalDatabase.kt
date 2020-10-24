package com.hirogakatageri.sandbox.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hirogakatageri.sandbox.local.dao.UserDao
import com.hirogakatageri.sandbox.local.model.LocalUserModel
import com.hirogakatageri.sandbox.local.model.SimpleLocalUserModel

@Database(
    entities = [
        LocalUserModel::class,
        SimpleLocalUserModel::class
    ], version = 2
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao

}