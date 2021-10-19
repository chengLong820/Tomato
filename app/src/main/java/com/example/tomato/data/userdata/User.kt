package com.example.tomato.data.userdata

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user")

data class User(@PrimaryKey val id: String,
                @ColumnInfo val password: String,
                var number: Int = 0)