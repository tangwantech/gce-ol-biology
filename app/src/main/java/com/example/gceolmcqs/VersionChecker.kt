package com.example.gceolmcqs

import android.content.pm.PackageManager
import com.example.gceolmcqs.repository.RestRepository

//import com.parse.ParseObject
//import com.parse.ParseQuery

class VersionChecker {
    fun getLatestVersion(id: String, onCheckVersionListener: OnCheckVersionListener){

        val params = hashMapOf("username" to id, "password" to id)
        RestRepository().query(RestRepository.GET_APP_VERSION, params, object: RestRepository.OnQueryListener{
            override fun onSuccess(result: String) {
                onCheckVersionListener.onResult(result)
            }

            override fun onError(error: String?) {

            }
        })
    }

    fun getInstalledVersion(packageManager: PackageManager, packageName: String): String{
        return try {
            packageManager.getPackageInfo(packageName, 0).versionName!!
        } catch (e: PackageManager.NameNotFoundException){
            "Unknown"
        }
    }
    interface OnCheckVersionListener{

        fun onResult(version: String)
        fun onError(error: String?)
    }

}