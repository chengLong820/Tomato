package com.example.tomato.data.userdata

import androidx.room.*
import com.example.tomato.data.UserWithTasks
import com.example.tomato.data.taskdata.Task

@Dao
interface UserDao {

    @Query("select * from user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun queryById(id: String):User

    @Delete
    fun delete(user: User)

    @Insert(entity = User::class)
    fun insert(user: User)

    @Transaction
    @Query("SELECT * FROM user")
    fun getUsersWithTasks():List<UserWithTasks>

    @Update
    fun updateUser(user: User)
}
