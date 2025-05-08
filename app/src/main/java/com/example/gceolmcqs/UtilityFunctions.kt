package com.example.gceolmcqs

import android.content.Context
import android.os.Build
import android.provider.Settings
import java.util.UUID

class UtilityFunctions {
    fun getDeviceId(applicationContext: Context): String{
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        return if (deviceId.isNullOrBlank()) {
            UUID.nameUUIDFromBytes((Build.BOARD + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT).toByteArray()).toString()
        }else{
            deviceId
        }
    }
}