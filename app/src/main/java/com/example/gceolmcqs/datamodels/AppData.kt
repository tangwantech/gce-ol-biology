package com.example.gceolmcqs.datamodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_data")
data class AppData(
    @ColumnInfo(name = "dictionary_data")
    val dictionaryData: String,
    @ColumnInfo(name = "notes_data")
    val notesData: String,
    @ColumnInfo(name = "paper1_data")
    val paper1Data: String,
    @ColumnInfo(name = "paper2_data")
    val paper2Data: String,
    @ColumnInfo(name = "exercises_data")
    val exercisesData: String,
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "index")
    val index: Int=1)
