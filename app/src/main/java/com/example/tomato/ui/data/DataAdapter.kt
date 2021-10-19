package com.example.tomato.ui.data

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.tomato.R

import com.example.tomato.data.taskdata.Task
import java.text.SimpleDateFormat

class DataAdapter(activity: Activity, val resourceId: Int, data: List<Task>) :
  ArrayAdapter<Task>(activity, resourceId, data) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View
        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
        } else {
            view = convertView
        }
        val itemNameTextView = view.findViewById<TextView>(R.id.item_name)
        val itemTypeTextView = view.findViewById<TextView>(R.id.item_type)
        val itemTimeTextView = view.findViewById<TextView>(R.id.item_time)
        val task = getItem(position)
        if (task != null) {
            itemNameTextView.text = task.name
            itemTimeTextView.text = (task.workingTime * 4 / 60000).toString() + "分钟"
            itemTypeTextView.text = task.taskType
        }
        return view
    }


}