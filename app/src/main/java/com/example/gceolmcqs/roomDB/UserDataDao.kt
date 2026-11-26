package com.example.gceolmcqs.roomDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gceolmcqs.datamodels.UserData

@Dao
interface UserDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUserData(userData: UserData)

    @Query("SELECT `index`, subscription, '' AS app_data FROM user_data_table")
    fun getUserData(): List<UserData>

    @Query("UPDATE user_data_table SET app_data = ''")
    fun clearBlobAppData()

    @Query("DELETE FROM user_data_table")
    fun deleteAll()

    @Update
    fun update(userData: UserData)

}