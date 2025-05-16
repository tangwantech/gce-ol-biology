package com.example.gceolmcqs.viewmodels


import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager
import com.example.gceolmcqs.MCQConstants


//import com.example.gceolmcqs.repository.RemoteRepoManager
//import com.example.gceolmcqs.repository.RemoteRepoManager.OnAppDataAvailableListener
import com.example.gceolmcqs.repository.RestRepository
import com.example.gceolmcqs.roomDB.AppDataDoa
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
import com.example.gceolmcqs.roomDB.SubjectPackageDao


class SplashActivityViewModel : ViewModel() {
    private val userDataManager = UserDataManager()

    fun beginSetup(id: String, context: Context, listener: UserDataManager.UserDataManagerListener){
        userDataManager.apply {
            initUserDataDao(context)
            initID(id)
            start(listener)
        }

    }


}