package com.example.tomato.ui.login

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tomato.R
import com.example.tomato.TomatoClockApplication
import com.example.tomato.data.userdata.User
import com.example.tomato.ui.main.MainActivity
import com.example.tomato.ui.set.SetActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlin.concurrent.thread

class RegisterActivity : AppCompatActivity() {
    companion object {
        lateinit var handler: Handler
    }
    private var etusername: EditText? = null
    private var etpasswords: EditText? = null
    private var etcheck: EditText? = null
    var username = ""
    var passwords = ""
    var check = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        etusername = findViewById(R.id.et_username_register)
        etpasswords = findViewById(R.id.et_passwords_register)
        etcheck = findViewById(R.id.et_check_register)

        val intent = Intent(applicationContext, LogActivity::class.java)

        button_true_register.setOnClickListener {
            username = etusername!!.editableText.toString() //获取登录界面用户输入
            passwords = etpasswords!!.editableText.toString()
            check = etcheck!!.text.toString()

            thread {
                val msg = Message()
                if (username == "" || passwords == "" || check == "") {
                    msg.what = 5
                } else {
                    if (passwords != check) {
                        msg.what = 6
                    } else {
                        val foundUser:User? = TomatoClockApplication.userDao.queryById(username)
                        foundUser?.let {
                            msg.what = 7
                        }?:let{
                            val user = User(username, passwords)
                            TomatoClockApplication.userDao.insert(user)
                            msg.what = 8
                        }
                    }
                }
                handler.sendMessage(msg)
            }

            handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        5 -> {
                            Toast.makeText(applicationContext, "用户名或密码不能为空", Toast.LENGTH_SHORT).show()
                        }
                        6 -> {
                            Toast.makeText(applicationContext, "两次输入的密码不一致", Toast.LENGTH_SHORT).show()
                        }
                        7 -> {
                            Toast.makeText(applicationContext,"该用户名已存在", Toast.LENGTH_SHORT).show()
                        }
                        8 -> {
                            Toast.makeText(applicationContext, "注册成功，返回登录界面", Toast.LENGTH_SHORT).show()
                            startActivity(intent)
                        }
                    }
                }
            }

        }

        button_false_register.setOnClickListener {
            startActivity(intent)
        }
    }
}