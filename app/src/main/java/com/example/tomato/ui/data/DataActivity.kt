package com.example.tomato.ui.data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Toast
import com.example.tomato.R
import com.example.tomato.TomatoClockApplication
import com.example.tomato.data.taskdata.Task
import com.example.tomato.ui.login.LogActivity
import com.example.tomato.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_data.*
import kotlin.concurrent.thread

class DataActivity : AppCompatActivity() {
    companion object{
        lateinit var handler: Handler
        lateinit var taskList: List<Task>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_data)
        val userID = TomatoClockApplication.currentUser
        thread {
            taskList = TomatoClockApplication.taskDao.queryByCreatorID(TomatoClockApplication.currentUser)
            val msg = Message()
            msg.what = 11
            handler.sendMessage(msg)
        }

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    11 ->{
                        val adapter = DataAdapter(this@DataActivity, R.layout.data_item, taskList)
                        data_list_view.adapter = adapter
                    }
                }
            }
        }
    }

}