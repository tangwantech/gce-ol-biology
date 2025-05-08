package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.AppSetupManager
//import com.example.gceolmcqs.AppDataUpdater
import com.example.gceolmcqs.SubjectPackageActivator
import com.example.gceolmcqs.UsageTimer
import com.example.gceolmcqs.VersionChecker
import com.example.gceolmcqs.datamodels.*
//import com.example.gceolmcqs.repository.AppDataLocalRepository
import com.example.gceolmcqs.repository.SubscriptionDataRepository
//import com.example.gceolmcqs.repository.RemoteRepoManager
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase
import kotlinx.coroutines.launch

class MainActivityViewModel : ViewModel() {
    private val subjectPackageDataList = ArrayList<SubjectPackageData>()
    private var indexOfCurrentSubject: Int? = null
    private val _usageTimeBonus = MutableLiveData<Long>()
    val usageTimeBonus: LiveData<Long> = _usageTimeBonus
    private val appSetupManager = AppSetupManager()


    fun updateSubjectPackageDataList() {
        val temp = appSetupManager.getSubscriptionData()
        subjectPackageDataList.clear()
        subjectPackageDataList.add(temp)
    }

//    fun initSubjectPackages(context: Context, listener: SubjectPackageDataRepository.OnQueryCallBackListener){
//        val subjectPackageDao = GceOLMcqDatabase.getDatabase(context).subjectPackageDao()
//
//        val subjectPackageDataRepository = SubjectPackageDataRepository(subjectPackageDao)
//        subjectPackageDataRepository.getSubjectPackageDataFromLocaldb(object: SubjectPackageDataRepository.OnQueryCallBackListener{
//            override fun onResult(subjectPackageData: SubjectPackageData?) {
//                subjectPackageDataList.clear()
//                subjectPackageDataList.add(subjectPackageData!!)
//                listener.onResult(subjectPackageData)
//            }
//
//            override fun onError() {
//            }
//
//        })
//    }



    fun updatePackageStatusAt(index: Int, updateCallBack: SubscriptionDataRepository.SubscriptionListener){
        subjectPackageDataList[index].isPackageActive = false
        appSetupManager.updateSubscriptionDataInLocaldb(subjectPackageDataList[index])
        updateCallBack.onSubscriptionUpdated()
//        updateSubjectPackageDataInRemoteDb(subjectPackageDataList[index], updateCallBack)
    }

    fun getSubjectPackageDataList(): ArrayList<SubjectPackageData>{
        return subjectPackageDataList
    }

//    private fun updateSubjectPackageDataInRemoteDb(subjectPackageData: SubjectPackageData,  updateCallBack: RemoteRepoManager.OnUpdatePackageListener){
//        RemoteRepoManager.updateSubjectPackagesForParseUser(subjectPackageData, object : RemoteRepoManager.OnUpdatePackageListener{
//            override fun onUpDateSuccessful(index: Int) {
//                updateCallBack.onUpDateSuccessful(index)
//            }
//
//            override fun onError() {
//                updateCallBack.onError()
//            }
//
//        })
//    }



//    fun initAppData(context: Context){
////        Paper1DataRepository.initPaper1Data()
//        val appDataDao = GceOLMcqDatabase.getDatabase(context).appDataDao()
//        val appDataLocalRepository = AppDataLocalRepository(appDataDao)
//        appDataLocalRepository.getPaper1DataString(object : AppDataLocalRepository.OnQueryAppDataListener{
//            override fun onResult(data: String) {
//                if (data != "NA")
//                Paper1DataRepository.initPaperData(data)
//            }
//
//        })
////        CoroutineScope(Dispatchers.IO).launch {
////            queryTest()
////        }
//
//    }

//    fun updateAppData(appDataUpdateListener: AppDataUpdater.AppDataUpdateListener) {
//        AppDataUpdater.update(appDataUpdateListener)
//    }



    fun setIndexOfCurrentSubject(index: Int){
        indexOfCurrentSubject = index
    }

    fun getIndexOfCurrentSubject(): Int?{
        return indexOfCurrentSubject
    }

    fun calculateNewBonusTime(oldBonus: Long, bonusTimeDiscount: Double){
        _usageTimeBonus.value = UsageTimer.getNewBonusTime(oldBonus, bonusTimeDiscount)
    }

    fun resetUsageTimer(){
        UsageTimer.resetUsageTimerData()
    }

    fun extentSubjectPackageAt(subjectIndex: Int, bonusTime: Long, isActive: Boolean, updateCallBack: SubscriptionDataRepository.SubscriptionListener){

        var subjectPackageData = getSubjectPackageDataList()[subjectIndex]
        val newExpiryDate = ActivationExpiryDatesGenerator.generateNewExpiryDate(subjectPackageData.expiresOn!!, bonusTime)
        subjectPackageData = SubjectPackageActivator.activateBonus(subjectPackageData, newExpiryDate)

        appSetupManager.updateSubscriptionDataInRemoteRepo(subjectPackageData, updateCallBack)
//        updateSubjectPackageDataInRemoteDb(subjectPackageData, updateCallBack)
    }

    fun checkForLatestVersionAvailable(id: String, onCheckVersionListener: VersionChecker.OnCheckVersionListener){
        viewModelScope.launch {
            VersionChecker().getLatestVersion(id, onCheckVersionListener)
        }

    }

    fun beginSetup(id: String, context: Context, listener: AppSetupManager.AppSetupListener){
        val userDataDao = GceOLMcqDatabase.getDatabase(context).userDataDao()
        appSetupManager.apply {
            initRepositories(id, userDataDao)
            start(listener)
        }
//        appSetupManager.start(listener)

    }

    fun isUserInitialised(): Boolean{
        return appSetupManager.isUserDataInitialised()
    }


}

