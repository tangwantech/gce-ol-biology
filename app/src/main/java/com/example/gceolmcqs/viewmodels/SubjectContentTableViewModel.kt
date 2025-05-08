package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.ActivationExpiryDatesGenerator
import com.example.gceolmcqs.AppSetupManager
import com.example.gceolmcqs.datamodels.ActivationExpiryDates

import com.example.gceolmcqs.datamodels.SubjectPackageData
//import com.example.gceolmcqs.repository.AppDataLocalRepository
import com.example.gceolmcqs.repository.Paper1DataRepository
import com.example.gceolmcqs.repository.SubscriptionDataRepository
//import com.example.gceolmcqs.repository.RemoteRepoManager
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase


class SubjectContentTableViewModel : ViewModel() {
    private lateinit var subjectName: String
    private val isSubjectPackageActive = MutableLiveData<Boolean>()
    private var subjectIndex: Int? = 0

    private val _subjectPackageData = MutableLiveData<SubjectPackageData>()
    val subjectPackageData: LiveData<SubjectPackageData> = _subjectPackageData

    private val appSetupManager = AppSetupManager()

    fun loadSubjectPackageDataFromSubscriptionRepository(){
        _subjectPackageData.value = SubscriptionDataRepository().getSubscription()
    }

//    fun loadSubjectPackageFromLocaldb(context: Context){
//        val subjectPackageDao = GceOLMcqDatabase.getDatabase(context).subjectPackageDao()
//        val subjectPackageDataRepository = SubjectPackageDataRepository(subjectPackageDao)
//        subjectPackageDataRepository.getSubjectPackageDataFromLocaldb(object : SubjectPackageDataRepository.OnQueryCallBackListener{
//            override fun onResult(subjectPackageData: SubjectPackageData?) {
//                _subjectPackageData.value = subjectPackageData!!
//            }
//
//            override fun onError() {
//
//            }
//
//        })
//    }



    fun getExamTitles(): List<String?> {

        return Paper1DataRepository.getExamTitles(subjectIndex!!)
    }

    fun getExamTypesCount(): Int {
        return Paper1DataRepository.getExamTitles(subjectIndex!!).size
    }

    fun getIsPackageActive(): LiveData<Boolean> {
        return isSubjectPackageActive
    }

    fun getPackageStatus(): Boolean{
        return ActivationExpiryDatesGenerator().checkExpiry(_subjectPackageData.value!!.activatedOn!!, _subjectPackageData.value!!.expiresOn!!)
    }

    fun setSubjectIndex(index: Int) {
        subjectIndex = index
    }

    fun getSubjectName(): String{
        return Paper1DataRepository.getSubjectName(subjectIndex!!)
    }

    fun getGraceExtension(): ActivationExpiryDates {
//        println("packageName: ${subjectPackageData.value!!.packageName!!}")
        return ActivationExpiryDatesGenerator.getGraceActivatedAndExpiryDate(_subjectPackageData.value!!.expiresOn!!, _subjectPackageData.value!!.packageName!!)
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