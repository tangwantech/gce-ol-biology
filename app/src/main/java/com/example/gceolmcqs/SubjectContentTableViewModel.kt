package com.example.gceolmcqs

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.datamodels.ActivationExpiryDates
import com.example.gceolmcqs.datamodels.SubjectPackageData
import com.example.gceolmcqs.repository.Paper1DataRepository

class SubjectContentTableViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    
    companion object {
        private const val KEY_SUBJECT_INDEX = "subjectIndex"
        private const val KEY_TAB_INDEX = "currentTabIndex"
    }

    private val isSubjectPackageActive = MutableLiveData<Boolean>()
    
    private var _subjectIndex: Int
        get() = savedStateHandle[KEY_SUBJECT_INDEX] ?: 0
        set(value) { savedStateHandle[KEY_SUBJECT_INDEX] = value }

    private var _currentTabIndex: Int
        get() = savedStateHandle[KEY_TAB_INDEX] ?: 0
        set(value) { savedStateHandle[KEY_TAB_INDEX] = value }

    private val _subjectPackageData = MutableLiveData<SubjectPackageData?>()
    val subjectPackageData: LiveData<SubjectPackageData?> = _subjectPackageData

    private val userDataManager = UserDataManager()
    private val userScoresStatsTracker = UserScoresStatsTracker()

    fun getCurrentTabIndex(): Int = _currentTabIndex

    fun updateCurrentTabIndex(index: Int) {
        _currentTabIndex = index
    }

    fun loadSubjectPackageDataFromUserDataRepository() {
        _subjectPackageData.value = userDataManager.getSubjectPackageAt(_subjectIndex)
    }

    fun getExamTitles(): List<String?> = Paper1DataRepository.getExamTitles(_subjectIndex)

    fun getExamTypesCount(): Int = Paper1DataRepository.getExamTitles(_subjectIndex).size

    fun getIsPackageActive(): LiveData<Boolean> = isSubjectPackageActive

    fun getPackageStatus(): Boolean = userDataManager.isSubscriptionActiveAt(_subjectIndex)

    fun setSubjectIndex(index: Int) {
        _subjectIndex = index
    }

    fun getSubjectIndex(): Int = _subjectIndex

    fun getSubjectName(): String = Paper1DataRepository.getSubjectName(_subjectIndex)

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

    fun updateScoresStatsInRemoteServer(id: String){
        userScoresStatsTracker.updateUserScoreStatsInServer(id)
    }
}
