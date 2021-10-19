package com.example.tomato

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.tomato.data.TomatoDatabase
import com.example.tomato.data.taskdata.TaskDao
import com.example.tomato.data.userdata.UserDao
import kotlin.concurrent.thread

class TomatoClockApplication : Application() {

    companion object{
        @SuppressLint("StaticFieldLeak")
        lateinit var tomatoClockApplicationContext: Context
        lateinit var db: TomatoDatabase
        lateinit var taskDao: TaskDao
        lateinit var userDao: UserDao
        var currentUser = ""
        var numberOfTomatoes = 0
    }

    override fun onCreate() {
        super.onCreate()
        tomatoClockApplicationContext = applicationContext
        db = Room.databaseBuilder(
            applicationContext,
            TomatoDatabase::class.java, "database"
        ).build()
        taskDao = db.taskDao()
        userDao = db.userDao()
    }
}