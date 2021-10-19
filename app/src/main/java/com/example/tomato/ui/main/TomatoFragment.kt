package com.example.tomato.ui.main

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tomato.R
import com.example.tomato.TomatoClockApplication
import com.example.tomato.Utils
import com.example.tomato.data.taskdata.Task
import com.example.tomato.ui.data.DataActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_set.*
import kotlinx.android.synthetic.main.fragment_tomato.*
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class TomatoFragment : Fragment() {
    //记录状态的变量
    var isRunning = false
    var flag: Int = 1

    companion object {
        lateinit var handler: Handler
    }

    lateinit var mainViewModel: MainViewModel

    var task: Task? = Task("番茄钟","未定义类型", 25, 5,
        TomatoClockApplication.currentUser, Utils.READY)


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        // Inflate the layout for this fragment
        mainViewModel.workTime = activity?.intent?.getIntExtra(
            "workTime",
            25 * 60 * 1000
        )!!
        mainViewModel.restTime = activity?.intent?.getIntExtra(
            "restTime",
            5 * 60 * 1000
        )!!
        mainViewModel.userID = activity?.intent?.getStringExtra("user").toString()
//        task = activity?.intent?.getSerializableExtra("task") as Task?
        return inflater.inflate(R.layout.fragment_tomato, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //显示用户已获得番茄数目
        val numberOfTomatoes = view.findViewById<TextView>(R.id.number_of_tasks)
        var number = TomatoClockApplication.numberOfTomatoes
        numberOfTomatoes.text = number.toString()

        //显示状态
        val statusTextView = view.findViewById<TextView>(R.id.status_text_view)

        //查看历史记录
        val dataButton = view.findViewById<Button>(R.id.data_button)
        dataButton.setOnClickListener {
            val intent = Intent(context, DataActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(intent)
        }

        //设置自定义控件和动画
        val countDownView: CountDownView = view.findViewById(R.id.count_down_view)
        var valueAnimator: ValueAnimator =
            ObjectAnimator.ofFloat(0f, mainViewModel.workTime.toFloat())

        valueAnimator.interpolator = LinearInterpolator() // 线性插值器，匀速变化

        val toggleButton: ToggleButton = view.findViewById(R.id.toggle_button)
        toggleButton.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                statusTextView.text = "番茄钟已开启"
                if (isRunning){
                    valueAnimator.resume()
                }else{
                    activity?.findViewById<FloatingActionButton>(R.id.fab)?.hide()

                    countDownView.setTotalTime(mainViewModel.workTime)

                    valueAnimator.duration = mainViewModel.workTime.toLong() // 执行时间

                    valueAnimator.addUpdateListener { animation ->
                        val currentTime = animation.animatedValue as Float
                        countDownView.setCurrentTime(currentTime.toInt())
                    }
                    valueAnimator.start()
                    isRunning = true
                    valueAnimator.doOnEnd {
                        flag ++
                        val msg = Message()
                        msg.what = flag
                        handler.sendMessage(msg)
                    }
                }
            } else {
                valueAnimator.pause()
                statusTextView.text = "番茄钟已暂停"
            }
        }

        handler= object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                when (msg.what) {
                    2 -> {
                        Toast.makeText(context, "第一个番茄钟结束，开始休息", Toast.LENGTH_SHORT).show()

                        valueAnimator=
                        ObjectAnimator.ofFloat(0f, mainViewModel.restTime.toFloat())
                        valueAnimator.duration = mainViewModel.restTime.toLong() // 执行时间
                        valueAnimator.interpolator = LinearInterpolator()
                        countDownView.setTotalTime(mainViewModel.restTime)
                        valueAnimator.addUpdateListener { animation ->
                            val currentTime = animation.animatedValue as Float
                            countDownView.setCurrentTime(currentTime.toInt())
                        }
                        valueAnimator.doOnEnd {
                            flag ++
                            val msg = Message()
                            msg.what = flag
                            handler.sendMessage(msg)
                        }
                        valueAnimator.start()
                    }
                    3 -> {
                        Toast.makeText(context, "休息时间结束，开始第二个番茄钟", Toast.LENGTH_SHORT).show()
                        valueAnimator=
                            ObjectAnimator.ofFloat(0f, mainViewModel.workTime.toFloat())
                        valueAnimator.duration = mainViewModel.workTime.toLong() // 执行时间
                        valueAnimator.interpolator = LinearInterpolator()
                        countDownView.setTotalTime(mainViewModel.workTime)
                        valueAnimator.addUpdateListener { animation ->
                            val currentTime = animation.animatedValue as Float
                            countDownView.setCurrentTime(currentTime.toInt())
                        }
                        valueAnimator.doOnEnd {
                            flag ++
                            val msg = Message()
                            msg.what = flag
                            handler.sendMessage(msg)
                        }
                        valueAnimator.start()
                    }
                    4 -> {
                        valueAnimator=
                            ObjectAnimator.ofFloat(0f, mainViewModel.restTime.toFloat())
                        valueAnimator.duration = mainViewModel.restTime.toLong() // 执行时间
                        valueAnimator.interpolator = LinearInterpolator()
                        countDownView.setTotalTime(mainViewModel.restTime)
                        valueAnimator.addUpdateListener { animation ->
                            val currentTime = animation.animatedValue as Float
                            countDownView.setCurrentTime(currentTime.toInt())
                        }
                        valueAnimator.doOnEnd {
                            flag ++
                            val msg = Message()
                            msg.what = flag
                            handler.sendMessage(msg)
                        }
                        valueAnimator.start()
                    }
                    5 -> {
                        Toast.makeText(context, "休息时间结束，开始第三个番茄钟", Toast.LENGTH_SHORT).show()
                        valueAnimator=
                            ObjectAnimator.ofFloat(0f, mainViewModel.workTime.toFloat())
                        valueAnimator.duration = mainViewModel.workTime.toLong() // 执行时间
                        valueAnimator.interpolator = LinearInterpolator()
                        countDownView.setTotalTime(mainViewModel.workTime)
                        valueAnimator.addUpdateListener { animation ->
                            val currentTime = animation.animatedValue as Float
                            countDownView.setCurrentTime(currentTime.toInt())
                        }
                        valueAnimator.doOnEnd {
                            flag ++
                            val msg = Message()
                            msg.what = flag
                            handler.sendMessage(msg)
                        }
                        valueAnimator.start()
                    }
                    6 -> {
                        Toast.makeText(context, "第三个番茄钟结束，开始休息", Toast.LENGTH_SHORT).show()
                        valueAnimator=
                            ObjectAnimator.ofFloat(0f, mainViewModel.restTime.toFloat())
                        valueAnimator.duration = mainViewModel.restTime.toLong() // 执行时间
                        valueAnimator.interpolator = LinearInterpolator()
                        countDownView.setTotalTime(mainViewModel.restTime)
                        valueAnimator.addUpdateListener { animation ->
                            val currentTime = animation.animatedValue as Float
                            countDownView.setCurrentTime(currentTime.toInt())
                        }
                        valueAnimator.doOnEnd {
                            flag ++
                            val msg = Message()
                            msg.what = flag
                            handler.sendMessage(msg)
                        }
                        valueAnimator.start()
                    }
                    7 -> {
                        Toast.makeText(context, "休息时间结束，开始第四个番茄钟", Toast.LENGTH_SHORT).show()
                        valueAnimator=
                            ObjectAnimator.ofFloat(0f, mainViewModel.workTime.toFloat())
                        valueAnimator.duration = mainViewModel.workTime.toLong() // 执行时间
                        valueAnimator.interpolator = LinearInterpolator()
                        countDownView.setTotalTime(mainViewModel.workTime)
                        valueAnimator.addUpdateListener { animation ->
                            val currentTime = animation.animatedValue as Float
                            countDownView.setCurrentTime(currentTime.toInt())
                        }
                        valueAnimator.doOnEnd {
                            flag ++
                            val msg = Message()
                            msg.what = flag
                            handler.sendMessage(msg)
                        }
                        valueAnimator.start()
                    }
                    8 -> {
                        isRunning = false
                        Toast.makeText(context, "番茄钟已完成", Toast.LENGTH_SHORT).show()
                        activity?.findViewById<FloatingActionButton>(R.id.fab)?.show()
                        statusTextView.text = "番茄钟已完成"
                        numberOfTomatoes.text = (number + 1).toString()
                        toggleButton.isChecked = false
                        thread {
                            task?.status = Utils.FINISHED
                            task?.let { TomatoClockApplication.taskDao.updateTask(it) }

                            val user = TomatoClockApplication.userDao.queryById(TomatoClockApplication.currentUser)
                            user.number++
                            TomatoClockApplication.userDao.updateUser(user)
                        }

                    }
                }
            }
        }

    }

    fun formatTime(time: Int): String? {
        val dateFormat = SimpleDateFormat("mm:ss")
        return dateFormat.format(time)
    }

    fun formatTime(time: Long): String? {
        val dateFormat = SimpleDateFormat("mm:ss")
        return dateFormat.format(time)
    }
}







