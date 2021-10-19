package com.example.tomato.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tomato.data.taskdata.Task
import com.example.tomato.data.taskdata.TaskDao
import com.example.tomato.data.userdata.User
import com.example.tomato.data.userdata.UserDao

@Database(entities = [Task::class, User::class], version = 1, exportSchema = false)
abstract class TomatoDatabase: RoomDatabase(){
    abstract fun taskDao(): TaskDao
    abstract fun userDao(): UserDao
}