package com.example.tomato.ui.set

import android.content.Intent
import android.icu.text.DateFormat.getDateTimeInstance
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tomato.R
import com.example.tomato.TomatoClockApplication
import com.example.tomato.Utils
import com.example.tomato.data.taskdata.Task
import com.example.tomato.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_set.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class SetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set)

        button_true.setOnClickListener{
            //设置番茄钟
            val user = TomatoClockApplication.currentUser
            val taskName = task_name.editableText.toString()
            val taskType = task_type.editableText.toString()
            val workTime = Integer.parseInt(work_time.editableText.toString())*60*1000 //时间从分钟换成毫秒
            val restTime = Integer.parseInt(rest_time.editableText.toString())*60*1000
            val createTime = getDateTimeInstance()

            //创建task实例并存入数据库
            val task = Task(taskName, taskType, workTime,restTime, user, Utils.READY)
            thread {
                TomatoClockApplication.taskDao.insert(task)
            }

            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("workTime", workTime)
            intent.putExtra("restTime", restTime)
            intent.putExtra("taskName", taskName)
            intent.putExtra("taskType", taskType)
            intent.putExtra("createTime", createTime)
            intent.putExtra("task",task)
            startActivity(intent)

        }
        button_false.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}