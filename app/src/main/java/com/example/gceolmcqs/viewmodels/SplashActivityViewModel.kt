package com.example.gceolmcqs.viewmodels


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.AppSetupManager
import com.example.gceolmcqs.MCQConstants
import com.example.gceolmcqs.datamodels.AppData
import com.example.gceolmcqs.datamodels.SubjectPackageData


//import com.example.gceolmcqs.repository.RemoteRepoManager
//import com.example.gceolmcqs.repository.RemoteRepoManager.OnAppDataAvailableListener
import com.example.gceolmcqs.repository.RestRepository
import com.example.gceolmcqs.roomDB.AppDataDoa
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
import com.example.gceolmcqs.roomDB.SubjectPackageDao
import com.google.gson.Gson


class SplashActivityViewModel : ViewModel() {
    private var appDataDoa: AppDataDoa? = null
    private var subjectPackageDao: SubjectPackageDao? = null
    private var db: GceOLMcqDatabase? = null
//    private var appDataRepository: AppDataLocalRepository? = null


    private val restRepository: RestRepository = RestRepository()


    fun signUpUser(params: HashMap<String, String>, listener: RestRepository.OnQueryListener){
//        restRepository = RestRepository()

        restRepository.query(RestRepository.SIGN_UP, params, listener)


    }

    fun getAppDataFromRest(id: String, listener: RestRepository.OnQueryListener){
        val appDataId = "JuBdoVeh8Z"
        val params = hashMapOf(MCQConstants.USER_NAME to id, MCQConstants.PASS_WORD to id, MCQConstants.OBJECT_ID to appDataId)
        println("processing getAppDataFromRest")
        restRepository.query(RestRepository.GET_APP_DATA, params, listener)

    }



    fun getAppVersion(id: String, listener: RestRepository.OnQueryListener){
        val params = hashMapOf(MCQConstants.USER_NAME to id, MCQConstants.PASS_WORD to id)
        restRepository.query(RestRepository.GET_APP_VERSION, params, listener)

    }


    fun beginSetup(id: String, context: Context, listener: AppSetupManager.AppSetupListener){
        val userDataDao = GceOLMcqDatabase.getDatabase(context).userDataDao()
        AppSetupManager().apply {
            initRepositories(id, userDataDao)
            start(listener)
        }
//        appSetupManager.start(listener)

    }


}