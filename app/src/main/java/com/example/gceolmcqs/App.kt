package com.example.gceolmcqs

import android.app.Application

//importandroid.app.Application
import android.os.Build
import android.provider.Settings
import com.example.gceolmcqs.datamodels.AppData
//import com.example.gceolmcqs.repository.AppDataLocalRepository
import com.example.gceolmcqs.repository.Paper1DataRepository
//import com.example.gceolmcqs.repository.RemoteRepoManager
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
//import com.parse.Parse
//import net.compay.android.CamPay
import java.util.UUID

class App: Application() {
    override fun onCreate() {
        super.onCreate()
//        initParse()
//        initRemoteRepoManager()
//        initCampay()


    }

//    private fun initParse(){
//        Parse.initialize(
//            Parse.Configuration.Builder(this)
//                .applicationId(getString(R.string.back4app_app_id))
//                // if defined
//                .clientKey(getString(R.string.back4app_client_key))
//                .server(getString(R.string.back4app_server_url))
//                .build()
//        )
//    }

    private fun initRemoteRepoManager(){
//        val deviceId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
//        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
//        RemoteRepoManager.setDeviceID(readDeviceId())
    }

    private fun readDeviceId(): String{
        val deviceId = Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID)
        return if (deviceId.isNullOrBlank()) {
            UUID.nameUUIDFromBytes((Build.BOARD + Build.MANUFACTURER + Build.MODEL + Build.PRODUCT).toByteArray()).toString()
        }else{
            deviceId
        }
    }

//    private fun initAppData(){
//        println("Initialization in App started...")
//        val appDataDao = GceOLMcqDatabase.getDatabase(this).appDataDao()
//        val appDataLocalRepository = AppDataLocalRepository(appDataDao)
//        appDataLocalRepository.loadAppDataFromLocaldb(object: AppDataLocalRepository.OnGetAppDataListener{
//            override fun onGetAppData(appData: AppData?) {
//                appData?.let{
//                    println("Innitialising paper1 data")
//                    initPaper1Data(appData.paper1Data)
//                }
//
//            }
//
//        })
//
//    }
//
//    fun initPaper1Data(dataString: String){
//        Paper1DataRepository.initPaperData(dataString)
//    }
    fun initPaper2Data(dataString: String){

    }

    fun initDictionaryData(dataString: String){

    }

    fun initExercisesData(dataString: String){

    }

    fun initNotesData(dataString: String){

    }

    companion object{
        val test = "test"
    }


//    private fun initCampay() {
//        CamPay.init(
//            getString(R.string.campay_app_user_name),
//            getString(R.string.campay_app_pass_word),
//            CamPay.Environment.DEV // environment
//        )
//    }


}