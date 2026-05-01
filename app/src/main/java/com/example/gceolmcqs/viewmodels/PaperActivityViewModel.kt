package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager
import com.example.gceolmcqs.UsageTimer
import com.example.gceolmcqs.datamodels.*
import com.example.gceolmcqs.repository.Paper1DataRepository
import com.example.gceolmcqs.repository.PaperRepository

class PaperActivityViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    
    companion object {
        private const val KEY_FRAGMENT_INDEX = "currentFragmentIndex"
        private const val KEY_SUBJECT_NAME = "subjectName"
        private const val KEY_SUBJECT_INDEX = "subjectIndex"
        private const val KEY_EXAM_TYPE_INDEX = "examTypeIndex"
        private const val KEY_EXAM_ITEM_INDEX = "examItemIndex"
        private const val KEY_SECTION_INDEX = "currentSectionIndex"
        private const val KEY_RESULT_DATA = "sectionResultData"
        private const val KEY_USER_ANSWERS = "userMarkedAnswers"
    }

    private var currentFragmentIndexProp: Int?
        get() = savedStateHandle[KEY_FRAGMENT_INDEX]
        set(value) { savedStateHandle[KEY_FRAGMENT_INDEX] = value }

    private var subjectNameProp: String?
        get() = savedStateHandle[KEY_SUBJECT_NAME]
        set(value) { savedStateHandle[KEY_SUBJECT_NAME] = value }

    private var subjectIndexProp: Int
        get() = savedStateHandle[KEY_SUBJECT_INDEX] ?: 0
        set(value) { savedStateHandle[KEY_SUBJECT_INDEX] = value }

    private var examTypeIndexProp: Int
        get() = savedStateHandle[KEY_EXAM_TYPE_INDEX] ?: 0
        set(value) { savedStateHandle[KEY_EXAM_TYPE_INDEX] = value }

    private var examItemIndexProp: Int
        get() = savedStateHandle[KEY_EXAM_ITEM_INDEX] ?: 0
        set(value) { savedStateHandle[KEY_EXAM_ITEM_INDEX] = value }

    private val userDataManager = UserDataManager()

    fun updateSubjectIndex(index: Int){
        subjectIndexProp = index
    }

    fun getSubjectName(): String{
        return subjectNameProp ?: ""
    }

    fun getSubjectIndex(): Int{
        return subjectIndexProp
    }

    fun updateExamTypeIndex(index: Int){
        examTypeIndexProp = index
    }

    fun updateExamItemIndex(index: Int){
        examItemIndexProp = index
    }

    fun getExamItemIndex(): Int {
        return examItemIndexProp
    }

    fun getExamItemTitle(): String{
        return Paper1DataRepository.getExamItemTitle(subjectIndexProp, examTypeIndexProp, examItemIndexProp)
    }

    fun setCurrentFragmentIndex(index: Int){
        currentFragmentIndexProp = index
    }

    fun getCurrentFragmentIndex(): Int? {
        return currentFragmentIndexProp
    }

    fun setSubjectName(subjectName: String) {
        this.subjectNameProp = subjectName
    }

    fun getUnAnsweredSectionIndexes(): List<Int>{
        return PaperRepository.getUnAnsweredSectionIndexes()
    }

    fun setCurrentSectionIndex(sectionIndex: Int){
        savedStateHandle[KEY_SECTION_INDEX] = sectionIndex
        PaperRepository.setCurrentSectionIndex(sectionIndex)
    }

    fun getCurrentSectionIndex(): Int {
        return savedStateHandle[KEY_SECTION_INDEX] ?: PaperRepository.getCurrentSectionIndex()
    }

    fun getTotalNumberOfQuestions():Int{
        return PaperRepository.getTotalNumberOfQuestions()
    }

    fun getSectionData(position: Int):SectionData?{
        return PaperRepository.getSectionDataAt(position)
    }

    fun getNumberOfSections(): Int{
        return PaperRepository.getNumberOfSections()
    }

    fun updateSectionsScore(sectionIndex: Int, score: Int){
        PaperRepository.updateSectionScoreAt(sectionIndex, score)
    }

    fun resetSectionScore(sectionIndex: Int){
        PaperRepository.resetSectionScoreAt(sectionIndex)
    }

    fun updateIsSectionsAnswered(sectionIndex: Int){
        PaperRepository.updateSectionsAnsweredAt(sectionIndex)
    }

    fun getIsSectionsAnswered(): List<Boolean>{
        return PaperRepository.getSectionsAnswered()
    }

    fun decrementCurrentSectionRetryCount(){
        PaperRepository.decrementCurrentSectionRetryCount()
    }

    fun resetCurrentSectionRetryCount(){
        PaperRepository.resetCurrentSectionRetryCount()
    }

    fun getCurrentSectionRetryCount(): LiveData<Int>{
        return PaperRepository.getCurrentSectionRetryCount()
    }

    fun setUserMarkedAnswerSheet(userMarkedAnswersSheetData: UserMarkedAnswersSheetData){
        savedStateHandle[KEY_USER_ANSWERS] = userMarkedAnswersSheetData
        PaperRepository.setUserMarkedAnswerSheet(userMarkedAnswersSheetData)
    }

    fun getUserMarkedAnswerSheet(): UserMarkedAnswersSheetData? {
        return savedStateHandle[KEY_USER_ANSWERS] ?: PaperRepository.getUserMarkedAnswerSheet()
    }

    fun setSectionResultData(sectionResultData: SectionResultData){
        savedStateHandle[KEY_RESULT_DATA] = sectionResultData
        PaperRepository.setSectionResultData(sectionResultData)
    }

    fun getSectionResultData(): SectionResultData? {
        return savedStateHandle[KEY_RESULT_DATA] ?: PaperRepository.getSectionResultData()
    }

    fun resetPaperRepository(){
        savedStateHandle.remove<Int>(KEY_SECTION_INDEX)
        savedStateHandle.remove<SectionResultData>(KEY_RESULT_DATA)
        savedStateHandle.remove<UserMarkedAnswersSheetData>(KEY_USER_ANSWERS)
        PaperRepository.resetPaperRepo()
    }

    fun startUsageTime(subjectIndex: Int) {
        val timeRemaining = userDataManager.getSubscriptionTimeRemainingAt(subjectIndex)
        UsageTimer.startUsageTimer(timeRemaining)
    }

    fun stopUsageTimer(){
        UsageTimer.stopTimer()
    }

    fun resetUsageTimerData() {
        UsageTimer.resetUsageTimerData()
    }

    fun isPackageActive(subjectIndex: Int): Boolean{
        return userDataManager.isSubscriptionActiveAt(subjectIndex)
    }

    private fun initPaper1DataRepository(){
        Paper1DataRepository.initPaper1Data(userDataManager.getPaper1Data())
    }

    private fun initPaperData(){
        val paperData = Paper1DataRepository.getPaperData(subjectIndexProp, examTypeIndexProp, examItemIndexProp)
        PaperRepository.initPaperData(subjectIndexProp, examTypeIndexProp, examItemIndexProp, paperData)
        
        // Restore session state into repository if it was stored in ViewModel
        val restoredSectionIndex = savedStateHandle.get<Int>(KEY_SECTION_INDEX)
        if (restoredSectionIndex != null) {
            PaperRepository.setCurrentSectionIndex(restoredSectionIndex)
        }
        
        val restoredResultData = savedStateHandle.get<SectionResultData>(KEY_RESULT_DATA)
        if (restoredResultData != null) {
            PaperRepository.setSectionResultData(restoredResultData)
        }
        
        val restoredAnswers = savedStateHandle.get<UserMarkedAnswersSheetData>(KEY_USER_ANSWERS)
        if (restoredAnswers != null) {
            PaperRepository.setUserMarkedAnswerSheet(restoredAnswers)
        }
    }

    fun isPaperDataInitialised(): Boolean{
        return PaperRepository.isPaperDataInitialised()
    }

    fun isPaper1DataInitialised(): Boolean{
        return Paper1DataRepository.isPaper1DataInitialised()
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

    fun initRepositories(){
        initPaper1DataRepository()
        initPaperData()
    }
}
