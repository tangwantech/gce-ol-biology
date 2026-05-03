package com.example.gceolmcqs

import android.app.Application
import android.os.Build
import android.provider.Settings
import com.example.gceolmcqs.repository.ExamScoreDataRepository
import com.example.gceolmcqs.repository.LocalUserDataRepository
import com.example.gceolmcqs.repository.Paper2DataRepository
import java.util.UUID

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Repositories
        LocalUserDataRepository.initUserDataDao(this)
//        Paper2DataRepository.initPaper2(this, "paper2.json")
        ExamScoreDataRepository.init(this)
    }

    private fun readDeviceId(): String{
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        return if (deviceId.isNullOrBlank()) {
            UUID.nameUUIDFromBytes((Build.BOARD + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT).toByteArray()).toString()
        }else{
            deviceId
        }
    }
}
