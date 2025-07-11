package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager

class NotesActivityViewModel: ViewModel() {
    private val userDataManager = UserDataManager()
    private var currentFragmentIndex = 0

    fun isUserDataInitialised():Boolean{
        return userDataManager.isUserDataInitialised()
    }
    fun isNotesDataInitialised(): Boolean{
        return userDataManager.getIsNotesInitialised()
    }

    fun beginSetup(id: String, context: Context, listener: UserDataManager.UserDataManagerListener){
        userDataManager.apply {
            initUserDataDao(context)
            initID(id)
            start(listener)
        }

    }

    fun initNotesDataRepository(){
        userDataManager.initDictionaryRepository()
        userDataManager.initNotesDataRepository()
    }

    fun getChapterNames(): List<String>{
        return userDataManager.getPaper1ChapterNames()
    }

    fun getFilePath(chapterIndex: Int): String{
        val filePath = userDataManager.getFilePath(chapterIndex)
        return filePath
    }

    fun getChapterExerciseNumbers(chapterIndex: Int): List<String>{
        return userDataManager.getChapterExerciseNumbers(chapterIndex)
    }
    fun updateCurrentFragmentIndex(index: Int){
        currentFragmentIndex = index
    }
    fun getCurrentFragmentIndex(): Int{
        return currentFragmentIndex

    }

    fun getDefinition(term: String): String{
        return userDataManager.getDefinition(term)
    }

}