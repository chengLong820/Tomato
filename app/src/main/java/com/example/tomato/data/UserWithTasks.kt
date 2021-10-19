package com.example.tomato.data

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation
import androidx.room.RoomWarnings
import com.example.tomato.data.taskdata.Task
import com.example.tomato.data.userdata.User


data class UserWithTasks (
    @SuppressWarnings(RoomWarnings.PRIMARY_KEY_FROM_EMBEDDED_IS_DROPPED)
    @Embedded val user:User,
    @Relation(
        parentColumn = "id",
        entityColumn = "userCreatorID"
    )
    val tasks:List<Task>
)