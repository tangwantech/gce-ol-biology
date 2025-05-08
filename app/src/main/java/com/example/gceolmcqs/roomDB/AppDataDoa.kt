package com.example.gceolmcqs.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gceolmcqs.datamodels.AppData

@Dao
interface AppDataDoa {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(appData: AppData)

    @Query("SELECT * FROM app_data")
    fun getAppData():List<AppData>

    @Update
    fun update(appData: AppData)
}