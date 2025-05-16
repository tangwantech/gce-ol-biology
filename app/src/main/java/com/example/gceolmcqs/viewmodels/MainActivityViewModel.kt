package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.UserDataManager
//import com.example.gceolmcqs.AppDataUpdater
import com.example.gceolmcqs.SubjectPackageActivator
import com.example.gceolmcqs.UsageTimer
import com.example.gceolmcqs.VersionChecker
import com.example.gceolmcqs.datamodels.*
import com.example.gceolmcqs.repository.LocalUserDataRepository
//import com.example.gceolmcqs.repository.AppDataLocalRepository

//import com.example.gceolmcqs.repository.RemoteRepoManager
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private var indexOfCurrentSubject: Int? = null
    private val userDataManager = UserDataManager()



    fun updateSubscriptionData(subjectPackageData: SubjectPackageData, listener: LocalUserDataRepository.OnUpdateUserDataListener){
        userDataManager.updateSubscriptionDataInLocaldb(subjectPackageData, listener)
    }

    fun getSubjectPackageData(): SubjectPackageData{
        return userDataManager.getSubscriptionData()
    }

    fun setIndexOfCurrentSubject(index: Int){
        indexOfCurrentSubject = index
    }

    fun getIndexOfCurrentSubject(): Int?{
        return indexOfCurrentSubject
    }

    fun checkForLatestVersionAvailable(id: String, onCheckVersionListener: VersionChecker.OnCheckVersionListener){
        viewModelScope.launch {
            VersionChecker().getLatestVersion(id, onCheckVersionListener)
        }

    }

    fun isUserDataInitialised():Boolean{
        return userDataManager.isUserDataInitialised()
    }

    fun beginSetup(id: String, context: Context, listener: UserDataManager.UserDataManagerListener){
        userDataManager.apply {
            initUserDataDao(context)
            initID(id)
            start(listener)
        }

    }



}

