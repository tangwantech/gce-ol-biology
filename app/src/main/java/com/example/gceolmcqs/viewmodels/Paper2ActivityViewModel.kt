package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager

class Paper2ActivityViewModel: ViewModel() {
    private val userDataManager = UserDataManager()

    private var currentFragmentIndex = 0

    private var subjectIndex = 0
    private var currentExamTypeIndex = 0
    private var currentExamTypeItemIndex = 0



    fun updateSubjectIndex(subjectIndex: Int){
        this.subjectIndex = subjectIndex
    }

    fun getSubjectIndex(): Int{
        return subjectIndex
    }

    fun updateCurrentExamTypeIndex(index: Int){
        currentExamTypeIndex = index
    }

    fun getCurrentExamTypeIndex(): Int{
        return currentExamTypeIndex
    }

    fun updateCurrentExamTypeItemIndex(index: Int){
        currentExamTypeItemIndex = index
    }

    fun getCurrentExamTypeItemIndex(): Int {
        return currentExamTypeItemIndex
    }

    fun isUserDataInitialised():Boolean{
        return userDataManager.isUserDataInitialised()
    }

    fun isPaper2DataInitialised(): Boolean{
        return userDataManager.getIsPaper2DataInitialised()
    }

    fun initPaper2DataRepository(context: Context){
        userDataManager.initDictionaryRepository()
        userDataManager.initPaper2DataRepository(context)
    }

    fun beginSetup(id: String, context: Context, listener: UserDataManager.UserDataManagerListener){
        userDataManager.apply {
            initUserDataDao(context)
            initID(id)
            start(listener)
        }

    }

    fun getDefinition(term: String): String{
        return userDataManager.getDefinition(term)
    }

    fun getSubjectName(subjectIndex: Int): String{
        return userDataManager.getPaper2SubjectNameAt(subjectIndex)
    }

    fun getPaper2ExamTitles(subjectIndex: Int): List<String>{
        return userDataManager.getPaper2ExamTitles(subjectIndex)
    }

    fun getPaper2ExamItemTitles(subjectIndex: Int, examTypeIndex: Int): List<String>{
        return userDataManager.getPaper2ExamItemTitles(subjectIndex, examTypeIndex)
    }

    fun getPaper2ExamItemTitleAt(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String {
        return userDataManager.getPaper2ExamItemTitle(subjectIndex, examTypeIndex, examItemIndex)
    }

    fun getPaper2FilePath(subjectIndex: Int, examTypeIndex: Int, examItemIndex: Int): String{
        return userDataManager.getPaper2FilePath(subjectIndex, examTypeIndex, examItemIndex)
    }

    fun isSubscriptionActiveAt(subjectIndex: Int): Boolean{
        return userDataManager.isSubscriptionActiveAt(subjectIndex)
    }

}