package com.example.gceolmcqs.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.gceolmcqs.UserDataManager

class DictionaryActivityViewModel: ViewModel() {
    private val userDataManager = UserDataManager()
    private val displayList = ArrayList<String>()



//    if userdata is initialised
//      init dictionaryRepo
//    else
//      beginSetup
//      OnSuccess
//        init dictionaryRepo

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

    fun initDictionaryRepo(){
        userDataManager.initDictionaryRepository()
        setDisplayList()
    }

    fun searchKey(keyword: String){
        displayList.clear()
        displayList.addAll(userDataManager.getAllMatches(keyword))
    }

    private fun setDisplayList(){
        displayList.clear()
        displayList.addAll(userDataManager.getDictionaryKeywords())
    }

    fun getDisplayList(): ArrayList<String>{
        return displayList
    }

    fun getDefinition(keyword: String): String{
        return userDataManager.getDefinition(keyword)
    }


}