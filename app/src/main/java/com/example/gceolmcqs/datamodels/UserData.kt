package com.example.gceolmcqs.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_data_table")
data class UserData(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    var index: Int=1,

    @ColumnInfo(name = "subscription")
    var subscription: String,

    @ColumnInfo(name = "app_data")
    var appData: String
)
