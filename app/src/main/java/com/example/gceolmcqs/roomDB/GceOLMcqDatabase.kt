package com.example.gceolmcqs.roomDB

import android.content.Context
import androidx.room.Database
//import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gceolmcqs.datamodels.AppData

import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.datamodels.UserData


@Database(entities = [UserData::class], version = 1)
//@TypeConverters(ScoresTypeConverter::class)
abstract class GceOLMcqDatabase: RoomDatabase() {

//    abstract fun subjectPackageDao(): SubjectPackageDao
//    abstract fun appDataDao(): AppDataDoa
    abstract fun userDataDao(): UserDataDao

    companion object {

        @Volatile
        private var INSTANCE: GceOLMcqDatabase? = null

        fun getDatabase(context: Context): GceOLMcqDatabase {

            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GceOLMcqDatabase::class.java,
                    "app_database12"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}