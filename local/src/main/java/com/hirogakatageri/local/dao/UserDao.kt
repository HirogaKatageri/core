package com.hirogakatageri.local.dao

import androidx.room.*
import com.hirogakatageri.local.model.LocalUserModel
import com.hirogakatageri.local.model.LocalUserModelSimple

@Dao
interface UserDao {

    @Query("SELECT * FROM users")
    fun getAllUsers(): List<LocalUserModel>

    @Query("SELECT rowid, username, followers, profile_image_url, notes FROM users LIMIT :offset, 20")
    fun getUsers(offset: Int): List<LocalUserModelSimple>

    @Query("SELECT * FROM users JOIN users_fts ON rowid == users_fts.rowid WHERE users_fts.username MATCH :value GROUP BY rowid")
    fun search(value: String): List<LocalUserModelSimple>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUsers(list: List<LocalUserModel>)

    @Update
    fun updateUser(vararg users: LocalUserModel)

}