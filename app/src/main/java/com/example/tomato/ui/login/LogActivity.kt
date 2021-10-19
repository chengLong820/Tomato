package com.example.tomato.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tomato.R
import com.example.tomato.TomatoClockApplication
import com.example.tomato.data.userdata.User
import com.example.tomato.ui.main.MainActivity
import kotlin.concurrent.thread


class LogActivity : AppCompatActivity() {
    companion object {
        lateinit var handler: Handler
    }

    private var etusername: EditText? = null
    private var etpasswords: EditText? = null
    var username = ""
    var passwords = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log)
        etusername = findViewById(R.id.et_username)
        etpasswords = findViewById(R.id.et_passwords)


        val registerButton = findViewById<Button>(R.id.register)
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        val logButton = findViewById<Button>(R.id.log)
        logButton.setOnClickListener {
            username = etusername!!.editableText.toString() //获取登录界面用户输入
            passwords = etpasswords!!.editableText.toString()

            thread {
                val msg = Message()
                val foundUser: User? = TomatoClockApplication.userDao.queryById(username)

                if (username == "" || passwords == "") {
                    msg.what = 1
                } else {
                    foundUser?.let {
                        //非空的情况
                        if (foundUser.password != passwords) {
                            msg.what = 2
                        } else {
                            msg.what = 3
                            TomatoClockApplication.numberOfTomatoes = foundUser.number
                        }
                    } ?:let {
                        //为空的情况
                        msg.what = 4
                    }
                }
                handler.sendMessage(msg)
            }
        }

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    1 -> {
                        Toast.makeText(applicationContext, "账号或密码不能为空", Toast.LENGTH_SHORT)
                            .show()
                    }
                    2 -> {
                        Toast.makeText(applicationContext, "密码错误", Toast.LENGTH_SHORT).show()
                    }
                    3 -> {
                        Toast.makeText(applicationContext, "登录成功", Toast.LENGTH_SHORT).show()
                        //登录成功，并将用户信息传给MainActivity和application
                        TomatoClockApplication.currentUser = username
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        intent.putExtra("user", username)
                        startActivity(intent)
                    }
                    4 -> {
                        Toast.makeText(applicationContext, "该用户不存在", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }

}