package com.example.tomato.data.taskdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "task")
data class Task(
    val name: String,
    @ColumnInfo(name = "type") val taskType: String,
    val workingTime: Int,
    val restTime: Int,
    @ColumnInfo val userCreatorID: String,
    var status: Int,
    val createTime: String = "",
    val startTime: String = "",
    val finishTime: String = ""
):Serializable{
    @PrimaryKey (autoGenerate = true)
    var id:Long = 0
}