package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager
import com.example.gceolmcqs.UsageTimer
import com.example.gceolmcqs.datamodels.*
import com.example.gceolmcqs.repository.Paper1DataRepository
import com.example.gceolmcqs.repository.PaperRepository
import com.example.gceolmcqs.roomDB.GceOLMcqDatabase

//import com.example.gceolmcqs.repository.RemoteRepoManager

class PaperActivityViewModel:ViewModel() {
    private var currentFragmentIndex: Int? = null
    private lateinit var subjectName: String
    private var subjectIndex = 0
    private var examTypeIndex = 0
    private var examItemIndex = 0

    private val userDataManager = UserDataManager()

    fun updateSubjectIndex(index: Int){
        subjectIndex = index
    }

    fun getSubjectName(): String{
        return subjectName
    }

    fun getSubjectIndex(): Int{
        return subjectIndex
    }

    fun updateExamTypeIndex(index: Int){
        examTypeIndex = index
    }

    fun updateExamItemIndex(index: Int){
        examItemIndex = index
    }

    fun getExamItemIndex(): Int {
        return examItemIndex
    }

    fun getExamItemTitle(): String{
        return Paper1DataRepository.getExamItemTitle(subjectIndex, examTypeIndex, examItemIndex)
    }

    fun setCurrentFragmentIndex(index: Int){
        currentFragmentIndex = index
    }

    fun getCurrentFragmentIndex():Int?{
        return currentFragmentIndex
    }

    fun setSubjectName(subjectName: String) {
        this.subjectName = subjectName
    }

    fun getUnAnsweredSectionIndexes(): List<Int>{
        return PaperRepository.getUnAnsweredSectionIndexes()
    }

    fun setCurrentSectionIndex(sectionIndex: Int){
        PaperRepository.setCurrentSectionIndex(sectionIndex)
    }

    fun getCurrentSectionIndex(): Int {
        return PaperRepository.getCurrentSectionIndex()
    }

    fun getTotalNumberOfQuestions():Int{
        return PaperRepository.getTotalNumberOfQuestions()
    }

    fun getSectionData(position: Int):SectionData{
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
        PaperRepository.setUserMarkedAnswerSheet(userMarkedAnswersSheetData)
    }

    fun getUserMarkedAnswerSheet(): UserMarkedAnswersSheetData {
        return PaperRepository.getUserMarkedAnswerSheet()
    }

    fun setSectionResultData(sectionResultData: SectionResultData){
        PaperRepository.setSectionResultData(sectionResultData)
    }

    fun getSectionResultData(): SectionResultData {
        return PaperRepository.getSectionResultData()
    }

    fun resetPaperRepository(){
        PaperRepository.resetPaperRepo()
    }

//    fun isPackageActive(subjectIndex: Int): Boolean{
//
//        val activatedOn = RemoteRepoManager.getSubjectPackageDataAtIndex(subjectIndex).activatedOn
//        val expiresOn = RemoteRepoManager.getSubjectPackageDataAtIndex(subjectIndex).expiresOn
//        return ActivationExpiryDatesGenerator().checkExpiry(activatedOn!!, expiresOn!!)
//    }

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
        val paperData = Paper1DataRepository.getPaperData(subjectIndex, examTypeIndex, examItemIndex)
        PaperRepository.initPaperData(subjectIndex, examTypeIndex, examItemIndex, paperData)

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