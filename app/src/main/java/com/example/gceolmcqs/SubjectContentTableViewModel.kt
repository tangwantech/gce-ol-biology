package com.example.gceolmcqs

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.datamodels.ActivationExpiryDates
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.repository.Paper1DataRepository

class SubjectContentTableViewModel : ViewModel() {
    private val isSubjectPackageActive = MutableLiveData<Boolean>()
    private var subjectIndex: Int = 0
    private var currentTabIndex: Int = 0

    private val _subjectPackageData = MutableLiveData<SubjectPackageData?>()
    val subjectPackageData: LiveData<SubjectPackageData?> = _subjectPackageData

    private val userDataManager = UserDataManager()

    fun getCurrentTabIndex(): Int = currentTabIndex

    fun updateCurrentTabIndex(index: Int) {
        currentTabIndex = index
    }

    fun loadSubjectPackageDataFromUserDataRepository() {
        _subjectPackageData.value = userDataManager.getSubjectPackageAt(subjectIndex)
    }

    fun getExamTitles(): List<String?> = Paper1DataRepository.getExamTitles(subjectIndex)

    fun getExamTypesCount(): Int = Paper1DataRepository.getExamTitles(subjectIndex).size

    fun getIsPackageActive(): LiveData<Boolean> = isSubjectPackageActive

    fun getPackageStatus(): Boolean = userDataManager.isSubscriptionActiveAt(subjectIndex)

    fun setSubjectIndex(index: Int) {
        subjectIndex = index
    }

    fun getSubjectName(): String = Paper1DataRepository.getSubjectName(subjectIndex)

    fun getGraceExtension(): ActivationExpiryDates? {
        val data = _subjectPackageData.value ?: return null
        val expiresOn = data.expiresOn ?: return null
        val packageName = data.packageName ?: return null
        return ActivationExpiryDatesGenerator.getGraceActivatedAndExpiryDate(expiresOn, packageName)
    }

    fun initPaper1DataRepository() {
        userDataManager.getPaper1Data()?.let {
            Paper1DataRepository.initPaper1Data(it)
        }
    }

    fun isPaper1DataInitialised(): Boolean = Paper1DataRepository.isPaper1DataInitialised()

    fun isUserDataInitialised(): Boolean = userDataManager.isUserDataInitialised()

    fun beginSetup(id: String, context: Context, listener: UserDataManager.UserDataManagerListener) {
        userDataManager.apply {
            initUserDataDao(context)
            initID(id)
            start(listener)
        }
    }
}
