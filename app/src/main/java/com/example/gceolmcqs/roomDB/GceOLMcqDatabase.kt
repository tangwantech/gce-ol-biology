package com.example.gceolmcqs.roomDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gceolmcqs.datamodels.ExamScoreEntity
import com.example.gceolmcqs.datamodels.UserData

@Database(entities = [UserData::class, ExamScoreEntity::class], version = 3)
abstract class GceOLMcqDatabase: RoomDatabase() {

    abstract fun userDataDao(): UserDataDao
    abstract fun examScoreDao(): ExamScoreDao

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
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}
